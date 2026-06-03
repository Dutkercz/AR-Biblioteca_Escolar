package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.LocatarioRequestDto;
import dutkercz.biblioteca.dto.LocatarioResponseDto;
import dutkercz.biblioteca.repository.LocatarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocatarioService {

    private final LocatarioRepository locatarioRepository;

    public LocatarioResponseDto cadastrarLocatario(LocatarioRequestDto locatarioRequestDto) {
        return null;
    }
}
