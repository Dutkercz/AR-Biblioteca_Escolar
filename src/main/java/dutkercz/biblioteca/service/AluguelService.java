package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.aluguel.AluguelRequestDto;
import dutkercz.biblioteca.dto.aluguel.AluguelResponseDto;
import dutkercz.biblioteca.exception.custom.BusinessException;
import dutkercz.biblioteca.mapper.AluguelMapper;
import dutkercz.biblioteca.model.Aluguel;
import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.model.Locatario;
import dutkercz.biblioteca.model.enums.AluguelStatus;
import dutkercz.biblioteca.repository.AluguelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AluguelService {
    private final AluguelRepository aluguelRepository;
    private final AluguelMapper aluguelMapper;
    private final LivroService livroRepository;
    private final LocatarioService locatarioService;

    @Transactional
    public AluguelResponseDto alugarLivro(AluguelRequestDto requestDto) {
        Locatario locatario = locatarioService.encontrarLocatarioPorId(requestDto.locatarioId());
        List<Livro> livros = requestDto.livrosIds().stream().map(id -> {
            Livro livro = livroRepository.encotrarLivroDisponivelParaAlugar(id);
            livro.setEstaLocado(true);
            return livro;
        }).toList();

        Aluguel aluguel = aluguelRepository.save(aluguelMapper.toEntity(requestDto, locatario, livros));
        return aluguelMapper.toResponseDto(aluguel);
    }

    public Page<AluguelResponseDto> listarAlugueis(Pageable pageable) {
        return aluguelRepository.findAllAtivasEFinalizadas(pageable)
                                .map(aluguelMapper::toResponseDto);
    }

    @Transactional
    public void finalizarAluguel(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
           .orElseThrow(() -> new EntityNotFoundException("Aluguel de id " + id + " não encontrado"));
        aluguel.getLivros().forEach(livro -> livro.setEstaLocado(false));
        aluguel.setStatus(AluguelStatus.FINALIZADO);
    }

    @Transactional
    public void deletarAluguel(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Aluguel de id " + id + " não encontrado"));
        if (aluguel.getStatus().equals(AluguelStatus.FINALIZADO) || aluguel.getStatus().equals(AluguelStatus.CANCELADO)) {
            throw new BusinessException("Este aluguel não pode ser cancelado");
        }
        aluguel.getLivros().forEach(livro -> livro.setEstaLocado(false));
        aluguel.setStatus(AluguelStatus.CANCELADO);
    }
}
