package dutkercz.biblioteca.controller;

import dutkercz.biblioteca.dto.autor.AutorComLivrosResponseDto;
import dutkercz.biblioteca.dto.autor.AutorRequestDto;
import dutkercz.biblioteca.dto.autor.AutorResponseDto;
import dutkercz.biblioteca.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<AutorResponseDto> cadastrarAutor(@RequestBody @Valid AutorRequestDto requestDto,
                                                           UriComponentsBuilder builder){
        AutorResponseDto responseDto = autorService.cadastrarAutor(requestDto);
        URI uri = builder.path("/api/autores/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @GetMapping()
    public ResponseEntity<Page<AutorResponseDto>> encontrarAutorPorNome(@RequestParam String nome,
                                                                       Pageable  pageable) {
        return ResponseEntity.ok(autorService.encontrarAutorPorNome(nome, pageable));
    }

    @GetMapping("{id}/livros")
    public ResponseEntity<AutorComLivrosResponseDto> encontrarLivrosDeAutor(@PathVariable Long id){
        return ResponseEntity.ok(autorService.encontrarLivrosPorAutorId(id));
    }
}
