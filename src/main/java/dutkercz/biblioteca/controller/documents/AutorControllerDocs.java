package dutkercz.biblioteca.controller.documents;

import dutkercz.biblioteca.dto.autor.AutorComLivrosResponseDto;
import dutkercz.biblioteca.dto.autor.AutorRequestDto;
import dutkercz.biblioteca.dto.autor.AutorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Autor", description = "Endpoints para gerenciamento de Autores")
public interface AutorControllerDocs {

    @Operation(summary = "Realiza o cadastro de Autor",
            description = "Ao passar as informações necessárias realiza o cadastro de um Autor")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AutorResponseDto.class))}),
             @ApiResponse(responseCode = "404", description = "Entrada inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PostMapping
    ResponseEntity<AutorResponseDto> cadastrarAutor(@RequestBody @Valid AutorRequestDto requestDto,
                                                    UriComponentsBuilder builder);


    @Operation(summary = "Listar Autores",
            description = "Lista todos Autores com match no filtro de 'nome'")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Sucesso no request", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AutorResponseDto.class)))}),
             @ApiResponse(responseCode = "404", description = "Entrada inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @GetMapping()
    ResponseEntity<Page<AutorResponseDto>> encontrarAutorPorNome(@RequestParam @Parameter(required = true) String nome,
                                                                 @ParameterObject Pageable pageable);

    @Operation(summary = "Autor com livros",
            description = "Retorna um Autor com todos os livros relacionados")
    @ApiResponses(value =
        {@ApiResponse(responseCode = "200", description = "Sucesso no request", content = {
                @Content(mediaType = "application/json", schema =
                @Schema(implementation = AutorComLivrosResponseDto.class))}),
         @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content),
         @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @GetMapping("{id}/livros")
    ResponseEntity<AutorComLivrosResponseDto> encontrarLivrosDeAutor(@PathVariable Long id);

    @Operation(summary = "Deleta um Autor",
            description = "Deleta um Autor desde que não tenha livros relacionados")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "204", description = "Sucesso sem conteudo"),
             @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarAutor(@PathVariable Long id);


    @Operation(summary = "Remover associação de Autor em um livro",
            description = "Remove o autor de um livro, retornando o Autor com a lista atualizada")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Sucesso no request", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = AutorComLivrosResponseDto.class))}),
             @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content),
             @ApiResponse(responseCode = "500", description = "Erro de servidor", content = @Content)})
    @PutMapping("/{id}/livro/{livroId}")
    ResponseEntity<AutorComLivrosResponseDto> removerAutoriaDeLivro(@PathVariable Long id,
                                                                    @PathVariable Long livroId);
}
