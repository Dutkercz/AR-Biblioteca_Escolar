package dutkercz.biblioteca.dto.livro;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record LivroRequestDto(
        @NotBlank
        @Size(min = 4, message = "O campo nome deve ter no mínimo 4 caracteres")
        @Pattern(regexp = "^[A-Za-z ]+$", message = "O campo nome esta fora do formato esperado")
        @Schema(example = "Memórias Postumas de Ciclano Cubas")
        String nome,

        @NotBlank
        @Pattern(regexp = "\\d{13}")
        String ISBN,

        @JsonFormat(pattern = "dd/MM/yyyy")
        @Schema(pattern = "dd/MM/yyyy", example = "27/08/1950")
        LocalDate dataPublicacao,

        @NotNull
        List<Long> autoresIds
        ) {
}
