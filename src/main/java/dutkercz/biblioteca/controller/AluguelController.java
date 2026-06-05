package dutkercz.biblioteca.controller;

import dutkercz.biblioteca.dto.aluguel.AluguelRequestDto;
import dutkercz.biblioteca.dto.aluguel.AluguelResponseDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.service.AluguelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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
}
