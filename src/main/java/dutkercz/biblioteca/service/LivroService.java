package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.livro.LivroRequestDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.mapper.LivroMapper;
import dutkercz.biblioteca.model.Autor;
import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.service.validacoes.ValidarListaDeAutores;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final dutkercz.biblioteca.repository.LivroRepository livroRepository;
    private final ValidarListaDeAutores validarListaDeAutores;
    private final LivroMapper livroMapper;

    @Transactional
    public LivroResponseDto cadastrarLivro(LivroRequestDto requestDto) {
        List<Autor> autores = validarListaDeAutores.validarIds(requestDto.autoresIds());
        Livro livro = livroMapper.toEntity(requestDto);
        autores.forEach(livro::addAutor);
        livroRepository.save(livro);
        return  livroMapper.toResponseDto(livro);
    }


    public Livro encotrarLivroDisponivelParaAlugar(Long livroId) {
        return livroRepository.findByIdAndEstaLocadoFalse(livroId)
          .orElseThrow(() -> new EntityNotFoundException("Livro com id " +  livroId  +
                                                         " não disponível ou não foi encontrado"));
    }


}
