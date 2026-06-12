package dutkercz.biblioteca.dto.livro;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record LivroResponseDto(
        Long id,
        String nome,
        String ISBN,
        
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataPublicacao,

        List<String> autoresNomes) {
}
