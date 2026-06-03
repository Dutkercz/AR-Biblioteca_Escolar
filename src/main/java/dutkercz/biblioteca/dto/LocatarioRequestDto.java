package dutkercz.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.biblioteca.model.enums.GeneroEnum;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record LocatarioRequestDto(
        @NotBlank
        @Size(min = 4, message = "O campo nome deve ter no mínimo 4 caracteres")
        @Pattern(regexp = "^[A-Za-z ]+$", message = "O campo nome esta fora do formato esperado")
        String nome,

        @NotBlank
        GeneroEnum sexo,

        @NotBlank
        String telefone,

        @NotBlank
        @Email
        String email,

        @NotNull
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @NotBlank
        @CPF(message = "O cpf está fora do formato esperado")
        String cpf
) {
}
