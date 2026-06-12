package dutkercz.biblioteca.controller.documents;

import dutkercz.biblioteca.dto.locatario.LocatarioRequestDto;
import dutkercz.biblioteca.dto.locatario.LocatarioResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "Locatario", description = "Endpoints para gerenciamento de Locatarios")
public interface LocatarioControllerDocs {

    @Operation(summary = "Realiza o cadastro de Locatario",
            description = "Ao passar as informações necessárias realiza o cadastro de um Locatario")
    @ApiResponses(value =
        {@ApiResponse(responseCode = "201", description = "sucesso", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = LocatarioResponseDto.class))}),
         @ApiResponse(responseCode = "404", description = "Entrada inválidas", content = @Content),
         @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PostMapping
    ResponseEntity<LocatarioResponseDto> cadastrarLocatario(@RequestBody @Valid LocatarioRequestDto requestDto,
                                                            UriComponentsBuilder builder);

    @Operation(summary = "Deleta um Locatario",
            description = "Deleta um Locatario desde que não tenha alugueis ativos")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "204", description = "Sucesso sem conteudo"),
             @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarLocatario(@PathVariable Long id);
}
