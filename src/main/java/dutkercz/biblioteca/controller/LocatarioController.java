package dutkercz.biblioteca.controller;

import dutkercz.biblioteca.dto.locatario.LocatarioRequestDto;
import dutkercz.biblioteca.dto.locatario.LocatarioResponseDto;
import dutkercz.biblioteca.service.LocatarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/locatarios")
@RequiredArgsConstructor
public class LocatarioController {

    private final LocatarioService locatarioService;

    @PostMapping
    public ResponseEntity<LocatarioResponseDto> cadastrarLocatario(@RequestBody @Valid LocatarioRequestDto requestDto,
                                                                  UriComponentsBuilder builder) {
        LocatarioResponseDto responseDto = locatarioService.cadastrarLocatario(requestDto);
        URI uri =  builder.path("/api/locatarios/{id}").buildAndExpand(responseDto.id()).toUri();
        return  ResponseEntity.created(uri).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLocatario(@PathVariable Long id) {
        locatarioService.deletarPorId(id);
        return ResponseEntity.ok().build();
    }
}
