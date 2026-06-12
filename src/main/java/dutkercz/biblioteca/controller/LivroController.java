package dutkercz.biblioteca.controller;

import dutkercz.biblioteca.controller.documents.LivroControllerDocs;
import dutkercz.biblioteca.dto.livro.LivroRequestDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
public class LivroController implements LivroControllerDocs {
    private final LivroService livroService;


    @Override
    public ResponseEntity<LivroResponseDto> cadastrarLivro(@RequestBody @Valid LivroRequestDto requestDto,
                                                           UriComponentsBuilder builder){
        LivroResponseDto responseDto = livroService.cadastrarLivro(requestDto);
        URI uri = builder.path("/api/livros/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @Override
    public ResponseEntity<Page<LivroResponseDto>> listarLivrosDisponiveis(Pageable pageable){
        return ResponseEntity.ok(livroService.listarLivrosDisponiveis(pageable));
    }

    @Override
    public ResponseEntity<Page<LivroResponseDto>> listarLivrosAlugados(Pageable pageable){
        return ResponseEntity.ok(livroService.listarLivrosIndisponiveis(pageable));
    }

    @Override
    public ResponseEntity<LivroResponseDto> encontrarLivroPorId(@PathVariable Long id){
        return ResponseEntity.ok(livroService.encontrarLivroPorId(id));
    }

    @Override
    public ResponseEntity<Void> deletarLivroPorId(@PathVariable Long id){
        livroService.deletarPorId(id);
        return ResponseEntity.ok().build();
    }
}













