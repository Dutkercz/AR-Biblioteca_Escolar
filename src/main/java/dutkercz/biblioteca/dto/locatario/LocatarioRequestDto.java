package dutkercz.biblioteca.dto.locatario;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.biblioteca.domain.enums.GeneroEnum;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record LocatarioRequestDto(
        @NotBlank(message = "O campo nome não pode estar em branco")
        @Size(min = 4, message = "O campo nome deve ter no mínimo 4 caracteres")
        @Pattern(regexp = "^[A-Za-z ]+$", message = "O campo nome esta fora do formato esperado")
        String nome,

        @NotNull(message = "opção inválida")
        GeneroEnum genero,

        @NotBlank(message = "O campo telefone não pode estar em branco")
        @Pattern(regexp = "^(\\d{2}-?\\d{9}|\\d{2}-?\\d-?\\d{4}-?\\d{4})$",
                message = "O campo telefone está fora do formato esperado")
        String telefone,

        @NotBlank(message = "O campo email não pode estar em branco")
        @Email(message = "O campo email está fora do formato esperado")
        String email,

        @NotNull(message = "O campo data de nascimento não pode estar em branco")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @NotBlank(message = "O campo cpf não pode estar em branco")
        @CPF(message = "O cpf está fora do formato esperado")
        String cpf
) {
}
