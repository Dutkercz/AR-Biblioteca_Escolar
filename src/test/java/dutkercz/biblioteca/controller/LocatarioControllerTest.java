package dutkercz.biblioteca.controller;

import dutkercz.biblioteca.controller.factory.FactoryHelper;
import dutkercz.biblioteca.dto.locatario.LocatarioRequestDto;
import dutkercz.biblioteca.model.Locatario;
import dutkercz.biblioteca.model.enums.GeneroEnum;
import dutkercz.biblioteca.repository.LocatarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
@ActiveProfiles("test")
class LocatarioControllerTest {

    @Autowired
    private LocatarioRepository locatarioRepository;

    @Autowired
    private JacksonTester<LocatarioRequestDto> jsonLocatario;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Deve registrar um Locatario corretamente quando as informações foram validas")
    void deveCadastrarLocatarioComSucesso() throws Exception {
        var locatarioDto = FactoryHelper.createLocatarioRequestDto();

        mockMvc.perform(post("/api/locatarios")
                                .content(jsonLocatario.write(locatarioDto).getJson())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(locatarioDto.nome()))
                .andExpect(jsonPath("$.email").value(locatarioDto.email()))
                .andExpect(jsonPath("$.telefone").value(locatarioDto.telefone()))
                .andDo(print());
    }


    @Test
    @DisplayName("Deve falhar ao usar um CPF inválido no cadastro de Locatario")
    void deveFalharAoCadastrarLocatario() throws Exception {
        var cpfIvalido = "111.111.111-11";
        var locatarioDto = new LocatarioRequestDto("Locatario", GeneroEnum.FEMININO, "55999009988",
                               "email2@email.com", LocalDate.of(2000, 1, 1),
                               cpfIvalido);

        mockMvc.perform(post("/api/locatarios")
                                .content(jsonLocatario.write(locatarioDto).getJson())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve falhar ao tentar cadastrar Locatario com um usar um CPF já registrado")
    void deveFalharAoCadastrarLocatarioComCpfJaCadastrado() throws Exception {
        var locatarioJaSalvo = locatarioRepository.save(FactoryHelper.createLocatario());
        var emailJaCadastrado = locatarioJaSalvo.getCpf();
        var locatarioDto = new LocatarioRequestDto("Locatario", GeneroEnum.MASCULINO, "55999009988",
                               emailJaCadastrado, LocalDate.of(2000, 1, 1),
                               "03214930010");

        mockMvc.perform(post("/api/locatarios")
                                .content(jsonLocatario.write(locatarioDto).getJson())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve falhar ao tentar cadastrar Locatario com um usar um EMAIL já registrado")
    void deveFalharAoCadastrarLocatarioComEmailJaCadastrado() throws Exception {
        var locatarioJaSalvo = locatarioRepository.save(FactoryHelper.createLocatario());
        var cpfJaCadastrado = locatarioJaSalvo.getEmail();
        var locatarioDto = new LocatarioRequestDto("Locatario", GeneroEnum.FEMININO, "55999009988",
                               "email@email.com", LocalDate.of(2000, 1, 1),
                               cpfJaCadastrado);

        mockMvc.perform(post("/api/locatarios")
                                .content(jsonLocatario.write(locatarioDto).getJson())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve de deletar Locatario com sucesso se não houver Alugueis ativos")
    void deveDeletarLocatarioComSucesso() throws Exception {
        var locatario = locatarioRepository.save(FactoryHelper.createLocatario());

        mockMvc.perform(delete("/api/locatarios/"+locatario.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertFalse(locatarioRepository.findById(locatario.getId()).isPresent());
    }
}