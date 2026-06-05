package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.locatario.LocatarioRequestDto;
import dutkercz.biblioteca.dto.locatario.LocatarioResponseDto;
import dutkercz.biblioteca.mapper.LocatarioMapper;
import dutkercz.biblioteca.model.Locatario;
import dutkercz.biblioteca.repository.LocatarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocatarioService {

    private final LocatarioRepository locatarioRepository;
    private final LocatarioMapper locatarioMapper;

    @Transactional
    public LocatarioResponseDto cadastrarLocatario(LocatarioRequestDto locatarioRequestDto) {
        var locatario = locatarioRepository.save(locatarioMapper.toEntity(locatarioRequestDto));
        return locatarioMapper.toResponseDto(locatario);
    }

    public Locatario encontrarLocatarioPorId(Long locatarioId) {
        return locatarioRepository.findById(locatarioId)
          .orElseThrow(() -> new EntityNotFoundException("Locatario com id " + locatarioId + " não encontrado"));
    }
}
