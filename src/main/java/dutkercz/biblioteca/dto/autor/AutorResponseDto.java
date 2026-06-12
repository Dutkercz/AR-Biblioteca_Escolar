package dutkercz.biblioteca.dto.autor;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.biblioteca.domain.enums.GeneroEnum;
import java.time.LocalDate;

public record AutorResponseDto(
        Long id,
        String nome,
        GeneroEnum genero,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento
) {
}
