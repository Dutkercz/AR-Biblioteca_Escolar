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

    public Locatario locatarioFinder(Long locatarioId){
        return locatarioRepository.findById(locatarioId).orElseThrow(
            () -> new EntityNotFoundException("Locatario com id " + locatarioId + " não encontrado"));
    }

    private boolean existeLocatarioPorCpf(String cpf) {
        return locatarioRepository.existsByCpf(cpf);
    }


    private boolean existeLocatarioPorEmail(String email) {
        return locatarioRepository.existsByEmail(email);
    }

    @Transactional
    public LocatarioResponseDto cadastrarLocatario(LocatarioRequestDto requestDto) {
        if (existeLocatarioPorCpf(requestDto.cpf()) || existeLocatarioPorEmail(requestDto.email())) {
            throw new BusinessException("Email e/ou CPF já cadastrados");
        }
        var locatario = locatarioRepository.save(locatarioMapper.toEntity(requestDto));
        return locatarioMapper.toResponseDto(locatario);
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
