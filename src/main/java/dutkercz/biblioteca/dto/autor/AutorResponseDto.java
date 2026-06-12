package dutkercz.biblioteca.dto.autor;

import dutkercz.biblioteca.domain.enums.GeneroEnum;
import java.time.LocalDate;

public record AutorResponseDto(
        Long id,
        String nome,
        GeneroEnum genero,
        LocalDate dataNascimento
) {
}
