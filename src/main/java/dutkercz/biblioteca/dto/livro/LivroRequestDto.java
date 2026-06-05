package dutkercz.biblioteca.dto.livro;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.biblioteca.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record LivroRequestDto(
        @NotBlank
        String nome,

        @NotBlank
        String ISBN,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataPublicacao,

        @NotNull
        List<Long> autoresIds
        ) {
}
