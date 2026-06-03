package dutkercz.biblioteca.dto;

import java.time.LocalDate;

public record LocatarioResponseDto(
        Long id,
        String nome,
        String telefone,
        String email
) {
}
