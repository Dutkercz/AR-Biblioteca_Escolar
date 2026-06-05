package dutkercz.biblioteca.dto.livro;

import dutkercz.biblioteca.model.Autor;

import java.time.LocalDate;
import java.util.List;

public record LivroResponseDto(
        Long id,
        String nome,
        String ISBN,
        LocalDate dataPublicacao,
        List<String> autoresNomes) {
}
