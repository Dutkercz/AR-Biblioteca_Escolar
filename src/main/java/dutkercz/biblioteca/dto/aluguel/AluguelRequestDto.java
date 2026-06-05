package dutkercz.biblioteca.dto.aluguel;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AluguelRequestDto(
        @NotNull
        List<Long> livrosIds,
        @NotNull
        Long locatarioId,
        LocalDate dataDevolucao
) {
}
