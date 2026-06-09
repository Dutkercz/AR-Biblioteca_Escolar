package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.locatario.LocatarioRequestDto;
import dutkercz.biblioteca.dto.locatario.LocatarioResponseDto;
import dutkercz.biblioteca.exception.custom.BusinessException;
import dutkercz.biblioteca.mapper.LocatarioMapper;
import dutkercz.biblioteca.model.Aluguel;
import dutkercz.biblioteca.model.Locatario;
import dutkercz.biblioteca.model.enums.AluguelStatus;
import dutkercz.biblioteca.repository.AluguelRepository;
import dutkercz.biblioteca.repository.LocatarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocatarioService {

    private final LocatarioRepository locatarioRepository;
    private final LocatarioMapper locatarioMapper;
    private final AluguelRepository aluguelRepository;

    private Locatario locatarioFinder(Long locatarioId){
        return locatarioRepository.findById(locatarioId).orElseThrow(
            () -> new EntityNotFoundException("Locatario com id " + locatarioId + " não encontrado"));
    }

    @Transactional
    public LocatarioResponseDto cadastrarLocatario(LocatarioRequestDto locatarioRequestDto) {
        var locatario = locatarioRepository.save(locatarioMapper.toEntity(locatarioRequestDto));
        return locatarioMapper.toResponseDto(locatario);
    }

    public Locatario encontrarLocatarioPorId(Long locatarioId) {
        return locatarioFinder(locatarioId);
    }

    @Transactional
    public void deletarPorId(Long id) {
        Locatario locatario = locatarioFinder(id);
        List<Aluguel> algueisDoLocatario =  aluguelRepository.findByLocatarioId(id);
        boolean possuiAtivo = algueisDoLocatario.stream()
                                .anyMatch(x -> x.getStatus() == AluguelStatus.ATIVO);
        if (possuiAtivo) {
            throw new BusinessException("Locatario possui alugueis em aberto");
        }
        algueisDoLocatario.forEach(al -> al.setLocatario(null));
        locatarioRepository.delete(locatario);
    }
}
