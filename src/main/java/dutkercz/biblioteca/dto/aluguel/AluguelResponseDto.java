package dutkercz.biblioteca.dto.aluguel;

import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.dto.locatario.LocatarioResponseDto;
import dutkercz.biblioteca.model.enums.AluguelStatus;

import java.time.LocalDate;
import java.util.List;

public record AluguelResponseDto(
        Long id,
        LocalDate dataRetirada,
        LocalDate dataDevolucao,
        LocatarioResponseDto locatario,
        AluguelStatus status,
        List<LivroResponseDto> livros
) {
}
