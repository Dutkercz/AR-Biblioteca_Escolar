package dutkercz.biblioteca.controller;

import dutkercz.biblioteca.dto.LocatarioRequestDto;
import dutkercz.biblioteca.dto.LocatarioResponseDto;
import dutkercz.biblioteca.service.LocatarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
