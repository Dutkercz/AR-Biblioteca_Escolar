package dutkercz.biblioteca.dto;

public record LocatarioResponseDto(
        Long id,
        String nome,
        String telefone,
        String email
) {
}
