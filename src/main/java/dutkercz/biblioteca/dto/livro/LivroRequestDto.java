package dutkercz.biblioteca.dto.livro;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public record LivroRequestDto(
        @NotBlank
        String nome,

        @NotBlank
        @Pattern(regexp = "\\d{13}")
        String ISBN,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataPublicacao,

        @NotNull
        List<Long> autoresIds
        ) {
}
