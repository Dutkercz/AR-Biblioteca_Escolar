package dutkercz.biblioteca.controller.documents;

import dutkercz.biblioteca.dto.livro.LivroRequestDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "Livros", description = "Endpoints para gerenciamento de Livros")
public interface LivroControllerDocs {


    @Operation(summary = "Realiza o cadastro de Livro",
            description = "Ao passar as informações necessárias realiza o cadastro de um Livro")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponseDto.class))}),
             @ApiResponse(responseCode = "404", description = "Entrada inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PostMapping
    ResponseEntity<LivroResponseDto> cadastrarLivro(@RequestBody @Valid LivroRequestDto requestDto,
                                                    UriComponentsBuilder builder);

    @Operation(summary = "Livros disponíveis",
            description = "Lista todos Livros disponíveis pra locação")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Sucesso no request", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LivroResponseDto.class)))}),
             @ApiResponse(responseCode = "404", description = "Entrada inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @GetMapping("/disponiveis")
    ResponseEntity<Page<LivroResponseDto>> listarLivrosDisponiveis(@ParameterObject Pageable pageable);


    @Operation(summary = "Livros Alugados",
            description = "Lista todos Livros alugados")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Sucesso no request", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LivroResponseDto.class)))}),
             @ApiResponse(responseCode = "404", description = "Entrada inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @GetMapping("/alugados")
    ResponseEntity<Page<LivroResponseDto>> listarLivrosAlugados(@ParameterObject Pageable pageable);


    @Operation(summary = "Encontrar por id",
            description = "Retorna um Livro com base no ID passado")
    @ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "Sucesso no request", content = {
                @Content(mediaType = "application/json", schema =
                @Schema(implementation = LivroResponseDto.class))}),
         @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content),
         @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @GetMapping("/{id}")
    ResponseEntity<LivroResponseDto> encontrarLivroPorId(@PathVariable Long id);


    @Operation(summary = "Deleta um Livro",
            description = "Deleta um Livro")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "204", description = "Sucesso sem conteudo"),
             @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarLivroPorId(@PathVariable Long id);
}
