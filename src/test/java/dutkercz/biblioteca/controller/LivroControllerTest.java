package dutkercz.biblioteca.controller;

import dutkercz.biblioteca.controller.factory.FactoryHelper;
import dutkercz.biblioteca.dto.livro.LivroRequestDto;
import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.repository.AutorRepository;
import dutkercz.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
class LivroControllerTest {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private JacksonTester<LivroRequestDto> jsonLivro;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AutorRepository autorRepository;


    @Test
    @DisplayName("Deve registrar um Livro corretamente quando as informações forem validas")
    void deveCadastrarLivroComSucesso() throws Exception {
        var autor = autorRepository.save(FactoryHelper.createAutor());
        var requestDto = FactoryHelper.createLivroRequestDto(autor.getId());

        mockMvc.perform(post("/api/livros")
                                .content(jsonLivro.write(requestDto).getJson())
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.nome").value(requestDto.nome()))
               .andExpect(jsonPath("$.ISBN").value(requestDto.ISBN()))
               .andExpect(jsonPath("$.dataPublicacao").value(
                       requestDto.dataPublicacao().toString()))
               .andDo(print());
    }

    @Test
    @DisplayName("Deve falahar ao registar um Livro quando o ISBN já estiver cadastrado")
    void deveFalharAoCadastrarIsbnRepetido() throws Exception {
        var autor = autorRepository.save(FactoryHelper.createAutor());
        var requestDto = FactoryHelper.createLivroRequestDto(autor.getId());
        String isbnRepetido = requestDto.ISBN();

        //salvo um livro antes de mandar o resquet para ja ter um ISBN igual o do request salvo no DB
        var livroSalvo = livroRepository.save(new Livro(null, requestDto.nome(), isbnRepetido,
                                requestDto.dataPublicacao(),false, List.of(autor)));

        mockMvc.perform(post("/api/livros")
                                .content(jsonLivro.write(requestDto).getJson())
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andDo(print());
        assertEquals(1, livroRepository.count());
    }


    @Test
    @DisplayName("Deve falahar ao registar um Livro quando a lista de autores estiver vazia")
    void deveFalharAoCadastrarSemIdDeAutor() throws Exception {
        var requestDto = new LivroRequestDto("Livro", "1234567891234",
                 LocalDate.of(1950,1,1), Collections.emptyList());

        mockMvc.perform(post("/api/livros")
                                .content(jsonLivro.write(requestDto).getJson())
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andDo(print());
        assertEquals(0, livroRepository.count());
    }

    @Test
    @DisplayName("Deve falhar ao usar um ISBN mal formado")
    void deveFalharAoCadastrarLivro() throws Exception {
        var isbnInvalido = "123456"; //deve ter 13 digitos
        var livroRequestDto = new LivroRequestDto("Livro", isbnInvalido,
                        LocalDate.of(1950,1,1), List.of(1L));

        mockMvc.perform(post("/api/livros")
                                .content(jsonLivro.write(livroRequestDto).getJson())
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    @DisplayName("Deve listar somente livros disponiveis para aluguel")
    void deveListarSomenteLivrosDisponiveis() throws Exception {
        var autor = autorRepository.save(FactoryHelper.createAutor());
        var livros = livroRepository.saveAll(FactoryHelper.listaLivros(autor.getId()));

        mockMvc.perform(get("/api/livros/disponiveis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(livros.get(0).getId()))
                .andExpect(jsonPath("$.content[0].nome").value("Livro1"))
                .andExpect(jsonPath("$.content[0].ISBN").value(livros.get(0).getISBN()))
                .andDo(print());
    }


    @Test
    @DisplayName("Deve listar somente livros alugados")
    void deveListarSomenteLivrosAlugados() throws Exception {
        var autor = autorRepository.save(FactoryHelper.createAutor());
        var livros = livroRepository.saveAll(FactoryHelper.listaLivros(autor.getId()));

        mockMvc.perform(get("/api/livros/alugados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(livros.get(1).getId()))
                .andExpect(jsonPath("$.content[0].nome").value("Livro1"))
                .andExpect(jsonPath("$.content[0].ISBN").value(livros.get(1).getISBN()))
                .andDo(print());
    }

    @Test
    @DisplayName("Deve retornar um livro ao passar um ID existente")
    void deveEncontrarLivroPorIdComSucesso() throws Exception {
        var autor = autorRepository.save(FactoryHelper.createAutor());
        var livro = livroRepository.save(FactoryHelper.createLivro(autor.getId()));

        mockMvc.perform(get("/api/livros/"+livro.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(livro.getId()))
               .andExpect(jsonPath("$.nome").value("Livro1"))
               .andExpect(jsonPath("$.ISBN").value(livro.getISBN()))
               .andDo(print());
    }

    @Test
    @DisplayName("Deve retornar erro ao passar um ID inexistente")
    void deveFalharAoProcurarLivro() throws Exception {
       long idInvalido = 1L;

        mockMvc.perform(get("/api/livros/"+idInvalido))
               .andExpect(status().isNotFound())
               .andDo(print());
    }




}
