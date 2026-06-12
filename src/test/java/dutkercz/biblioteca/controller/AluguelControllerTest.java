package dutkercz.biblioteca.controller;

import com.fasterxml.jackson.core.JsonParser;
import dutkercz.biblioteca.controller.factory.FactoryHelper;
import dutkercz.biblioteca.dto.aluguel.AluguelRequestDto;
import dutkercz.biblioteca.domain.enums.AluguelStatus;
import dutkercz.biblioteca.repository.AluguelRepository;
import dutkercz.biblioteca.repository.AutorRepository;
import dutkercz.biblioteca.repository.LivroRepository;
import dutkercz.biblioteca.repository.LocatarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@Transactional //faz um rollback ao final de cada teste
class AluguelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LocatarioRepository locatarioRepository;

    @Autowired
    private JacksonTester<AluguelRequestDto> jsonRequestDto;

    @Test
    @DisplayName("Deve alugar um livro com sucesso")
    void alugarLivro() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        var autor = autorRepository.save(FactoryHelper.createAutor());
        var locatario =  locatarioRepository.save(FactoryHelper.createLocatario());
        var livro =  livroRepository.save(FactoryHelper.createLivro(autor.getId()));
        var requestDto = FactoryHelper.createAluguelRequestDto(locatario.getId(), livro.getId());

        mockMvc.perform(post("/api/alugueis")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestDto.write(requestDto).getJson()))
               .andExpect(status().isCreated())
                .andExpect(jsonPath("$.livros[0].id").value(livro.getId()))
               .andExpect(jsonPath("$.dataRetirada").value(dtf.format(LocalDate.now())))
               .andExpect(jsonPath("$.locatario.id").value(locatario.getId()))
               .andExpect(jsonPath("$.livros.length()").value(1))
               .andDo(print());

    }

    @Test
    @DisplayName("Deve listar corretamente uma lista de alugueis ativos e finalizados")
    void listarAlugueis() throws Exception {
        var autor = autorRepository.save(FactoryHelper.createAutor());
        var locatario =  locatarioRepository.save(FactoryHelper.createLocatario());
        var livro =  livroRepository.save(FactoryHelper.createLivro(autor.getId()));

        var livros = aluguelRepository.saveAll(FactoryHelper.listaAluguel(locatario, List.of(livro)));

        mockMvc.perform(get("/api/alugueis"))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(livros.get(0).getId()))
                .andExpect(jsonPath("$.content[1].id").value(livros.get(1).getId()))
                .andDo(print());
                //alugueis cancelados não entram nesse request por isso o size é 2, mas a lista do factory salva 3
    }

    @Test
    @DisplayName("Deve devolver status 200 quando a devolução for bem sucedida")
    void devolverLivros() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var autor = autorRepository.save(FactoryHelper.createAutor());
        var locatario =  locatarioRepository.save(FactoryHelper.createLocatario());
        var livro =  livroRepository.save(FactoryHelper.createLivro(autor.getId()));
        var aluguel = aluguelRepository.save(FactoryHelper.createAluguel(locatario, List.of(livro)));

        mockMvc.perform(put("/api/alugueis/devolver/"+aluguel.getId()))
                .andExpect(jsonPath("$.id").value(aluguel.getId()))
                .andExpect(jsonPath("$.dataDevolucao").value(dtf.format(LocalDate.now())))
                .andExpect(jsonPath("$.locatario.id").value(locatario.getId()))
                .andExpect(jsonPath("$.status").value(AluguelStatus.FINALIZADO.toString()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("Deve deletar um aluguel com sucesso quando status for ATIVO")
    void deveDeletarAluguelComSucesso() throws Exception {
        var autor = autorRepository.save(FactoryHelper.createAutor());
        var locatario =  locatarioRepository.save(FactoryHelper.createLocatario());
        var livro =  livroRepository.save(FactoryHelper.createLivro(autor.getId()));
        var aluguel = aluguelRepository.save(FactoryHelper.createAluguel(locatario, List.of(livro)));

        mockMvc.perform(delete("/api/alugueis/"+aluguel.getId()))
               .andExpect(status().isNoContent())
               .andDo(print());
        Assertions.assertFalse(aluguelRepository.existsById(aluguel.getId()));
    }

    @Test
    @DisplayName("Deve falhar ao tentar deletar um aluguel com status FINALIZADO")
    void deveFalharAoDeletarAluguel() throws Exception {
        var autor = autorRepository.save(FactoryHelper.createAutor());
        var locatario =  locatarioRepository.save(FactoryHelper.createLocatario());
        var livro =  livroRepository.save(FactoryHelper.createLivro(autor.getId()));
        var aluguel = FactoryHelper.createAluguel(locatario, List.of(livro));
        aluguel.setStatus(AluguelStatus.FINALIZADO);
        aluguelRepository.save(aluguel);

        mockMvc.perform(delete("/api/alugueis/"+aluguel.getId()))
               .andExpect(status().isBadRequest()).andDo(print());
        Assertions.assertTrue(aluguelRepository.existsById(aluguel.getId()));
    }

    @Test
    void livrosAlugadosPorLocatario() throws Exception {
        var autor = autorRepository.save(FactoryHelper.createAutor());
        var locatario =  locatarioRepository.save(FactoryHelper.createLocatario());
        var livro =  livroRepository.save(FactoryHelper.createLivro(autor.getId()));
        aluguelRepository.save(FactoryHelper.createAluguel(locatario, List.of(livro)));

        mockMvc.perform(get("/api/alugueis/locatario/"+locatario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(livro.getId()))
                .andExpect(jsonPath("$[0].ISBN").value(livro.getISBN()))
                .andExpect(jsonPath("$.length()").value(1));
    }
}