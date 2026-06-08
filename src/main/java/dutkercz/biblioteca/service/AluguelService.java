package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.aluguel.AluguelRequestDto;
import dutkercz.biblioteca.dto.aluguel.AluguelResponseDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.exception.custom.BusinessException;
import dutkercz.biblioteca.mapper.AluguelMapper;
import dutkercz.biblioteca.mapper.LivroMapper;
import dutkercz.biblioteca.model.Aluguel;
import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.model.Locatario;
import dutkercz.biblioteca.model.enums.AluguelStatus;
import dutkercz.biblioteca.repository.AluguelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AluguelService {
    private final AluguelRepository aluguelRepository;
    private final AluguelMapper aluguelMapper;
    private final LivroService livroRepository;
    private final LocatarioService locatarioService;
    private final LivroMapper livroMapper;

    @Transactional
    public AluguelResponseDto alugarLivro(AluguelRequestDto requestDto) {
        Locatario locatario = locatarioService.encontrarLocatarioPorId(requestDto.locatarioId());
        List<Livro> livros = requestDto.livrosIds().stream().map(id -> {
            Livro livro = livroRepository.encontrarLivroDisponivelParaAlugar(id);
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

    //array 1 = [a, b, c]
    //array 2 = [d, e, f]
    //map = [ [a, b, c], [d, e, f]] <-array de arrays
    //flatMap = [a, b, c, d, e, f] <- array dos arrays
    public Set<LivroResponseDto> historicoDeAluguelPorLocatario(Long locatarioID) {
        List<Aluguel> alugueis = aluguelRepository.getByLocatarioId(locatarioID);
        Set<Livro> livroSet = alugueis.stream()
                                      .flatMap(aluguel -> aluguel.getLivros().stream())
                                      .collect(Collectors.toSet());

        return livroSet.stream().map(livroMapper::toResponseDto).collect(Collectors.toSet());
    }
}
