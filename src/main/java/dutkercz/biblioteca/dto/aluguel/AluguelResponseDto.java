package dutkercz.biblioteca.dto.aluguel;

import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.model.Locatario;

import java.time.LocalDate;
import java.util.List;

public record AluguelResponseDto(
        Long id,
        LocalDate dataRetirada,
        LocalDate dataDevolucao,
        Locatario locatario,
        List<Livro> livros
) {
}
