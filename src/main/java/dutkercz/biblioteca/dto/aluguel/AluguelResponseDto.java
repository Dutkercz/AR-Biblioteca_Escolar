package dutkercz.biblioteca.dto.aluguel;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.dto.locatario.LocatarioResponseDto;
import dutkercz.biblioteca.domain.enums.AluguelStatus;

import java.time.LocalDate;
import java.util.List;

public record AluguelResponseDto(
        Long id,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataRetirada,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataDevolucao,

        LocatarioResponseDto locatario,
        AluguelStatus status,
        List<LivroResponseDto> livros
) {
}
