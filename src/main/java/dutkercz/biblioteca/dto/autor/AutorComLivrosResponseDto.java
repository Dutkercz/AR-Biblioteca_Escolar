package dutkercz.biblioteca.dto.autor;

import dutkercz.biblioteca.dto.livro.LivroResponseDto;

import java.util.List;

public record AutorComLivrosResponseDto(
        String nomeAutor,
        List<LivroResponseDto> livros
) {
}
