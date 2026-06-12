package dutkercz.biblioteca.dto.autor;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.biblioteca.domain.enums.GeneroEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;

public record AutorRequestDto(
        @NotBlank(message = "O campo nome não pode estar em branco")
        @Size(min = 4, message = "O campo nome deve ter no mínimo 4 caracteres")
        @Pattern(regexp = "^[\\p{L} ]+$", message = "O campo nome esta fora do formato esperado")
        @Schema(example = "Ciclano de Asis")
        String nome,

        @NotNull(message = "opção inválida")
        GeneroEnum genero,


        @NotNull(message = "O campo data de nascimento não pode estar em branco")
        @Schema(pattern = "dd/mm/yyyy", example = "15/10/1995")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @NotBlank(message = "O campo cpf não pode estar em branco")
        @Schema(pattern = "76793704001") //cpf gerado em  https://www.4devs.com.br/gerador_de_cpf
        @CPF(message = "O cpf está fora do formato esperado")
        String cpf
) {
}
