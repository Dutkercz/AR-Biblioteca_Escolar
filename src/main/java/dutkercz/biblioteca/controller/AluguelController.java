package dutkercz.biblioteca.controller;

import dutkercz.biblioteca.dto.aluguel.AluguelRequestDto;
import dutkercz.biblioteca.dto.aluguel.AluguelResponseDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.service.AluguelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/api/alugueis")
@RequiredArgsConstructor
public class AluguelController {
    private final AluguelService aluguelService;

    @PostMapping
    public ResponseEntity<AluguelResponseDto> alugarLivro(@RequestBody AluguelRequestDto requestDto,
                                                          UriComponentsBuilder builder){
        AluguelResponseDto responseDto = aluguelService.alugarLivro(requestDto);
        URI uri = builder.path("/api/alugueis/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<AluguelResponseDto>> listarAlugeis(Pageable pageable){
        return ResponseEntity.ok(aluguelService.listarAlugueis(pageable));
    }
    @PutMapping("/devolver/{id}")
    public ResponseEntity<AluguelResponseDto> devolverLivros(@PathVariable Long id){
        AluguelResponseDto responseDto = aluguelService.finalizarAluguel(id);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluguel(@PathVariable Long id){
        aluguelService.deletarAluguel(id);
        return ResponseEntity.noContent().build();
    }

    // buscar alugueis de um ID (locatarioID) -> pegar todos alugueis (list) -> pegar todos os livros de cada aluguel
    //remover repetidos usando o Set
    @GetMapping("/locatario/{locatarioID}")
    public ResponseEntity<Set<LivroResponseDto>> livrosAlugadosPorLocatario(@PathVariable Long locatarioID){
        return ResponseEntity.ok(aluguelService.historicoDeLivrosLocadosPorLocatario(locatarioID));
    }
}
