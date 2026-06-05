package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.aluguel.AluguelRequestDto;
import dutkercz.biblioteca.dto.aluguel.AluguelResponseDto;
import dutkercz.biblioteca.mapper.AluguelMapper;
import dutkercz.biblioteca.model.Aluguel;
import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.model.Locatario;
import dutkercz.biblioteca.repository.AluguelRepository;
import lombok.RequiredArgsConstructor;
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
}
