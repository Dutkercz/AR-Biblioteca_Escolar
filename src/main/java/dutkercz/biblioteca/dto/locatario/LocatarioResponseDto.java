package dutkercz.biblioteca.dto.locatario;

public record LocatarioResponseDto(
        Long id,
        String nome,
        String telefone,
        String email
) {
}
