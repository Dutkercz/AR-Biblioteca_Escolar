package dutkercz.biblioteca.controller.documents;

import dutkercz.biblioteca.dto.aluguel.AluguelRequestDto;
import dutkercz.biblioteca.dto.aluguel.AluguelResponseDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Tag(name = "Aluguel", description = "Endpoints para gerenciamento Alugueis dos Livros")
public interface AluguelControllerDocs {

    @Operation(summary = "Realiza o Aluguel de Livros",
    description = "Ao passar as informações necessárias realiza o Aluguel de livros disponíveis")
    @ApiResponses(value =
        {@ApiResponse(responseCode = "201", description = "sucesso na locação", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelRequestDto.class))}),
        @ApiResponse(responseCode = "404", description = "Entrada inválidas", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PostMapping
    ResponseEntity<AluguelResponseDto> alugarLivro(@RequestBody @Valid AluguelRequestDto requestDto,
                                                   UriComponentsBuilder builder);


    @Operation(summary = "Listar alugueis",
            description = "Lista todos alugueis ativos e finalizados")
    @ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "Sucesso no request", content = {
            @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AluguelResponseDto.class)))}),
         @ApiResponse(responseCode = "500", description = "Erro de servidos", content = @Content)})
    @GetMapping
    ResponseEntity<Page<AluguelResponseDto>> listarAlugeis(@ParameterObject Pageable pageable);



    @Operation(summary = "Finalizar aluguel",
        description = "Endpoint representa a devolução dos livros, finalizando o Aluguel")
    @ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "Sucesso no request", content = {
            @Content(mediaType = "application/json", schema =  @Schema(implementation = AluguelResponseDto.class))}),
         @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content),
         @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PutMapping("/devolver/{id}")
    ResponseEntity<AluguelResponseDto> devolverLivros(@PathVariable Long id);


    @Operation(summary = "Deleta um aluguel",
            description = "Deleta um aluguel desde que não tenha sido finalizado")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "204", description = "Sucesso sem conteudo"),
             @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarAluguel(@PathVariable Long id);


    @Operation(summary = "Busca livros alugados por um locatário",
            description = "Mostra o histórico de livros alugados por um Locatario")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Sucesso no request",  content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LivroResponseDto.class)))}),
             @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @GetMapping("/locatario/{locatarioID}")
    ResponseEntity<Set<LivroResponseDto>> livrosAlugadosPorLocatario(@PathVariable Long locatarioID);
}
