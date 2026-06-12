package dutkercz.biblioteca.dto.aluguel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AluguelRequestDto(
        @NotNull
        List<Long> livrosIds,

        @NotNull
        Long locatarioId,

        @Schema(pattern = "dd/MM/yyyy", example = "10/08/2030")
        LocalDate dataDevolucao
) {
}
