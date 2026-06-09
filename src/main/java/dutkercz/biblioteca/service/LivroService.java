package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.livro.LivroRequestDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.exception.custom.BusinessException;
import dutkercz.biblioteca.mapper.LivroMapper;
import dutkercz.biblioteca.model.Autor;
import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.service.validacoes.ValidarAutores;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final dutkercz.biblioteca.repository.LivroRepository livroRepository;
    private final ValidarAutores validarAutores;
    private final LivroMapper livroMapper;

    public Livro finderLivroPorId(Long id){
        return livroRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("Livro com id "+ id + " não encontrado"));
    }

    @Transactional
    public LivroResponseDto cadastrarLivro(LivroRequestDto requestDto) {
        if (livroRepository.existsByISBN(requestDto.ISBN())){
            throw new EntityExistsException("ISBN já cadastrado");
        }

        List<Autor> autores = validarAutores.validarIds(requestDto.autoresIds());
        Livro livro = livroMapper.toEntity(requestDto);
        autores.forEach(livro::addAutor);
        livroRepository.save(livro);
        return  livroMapper.toResponseDto(livro);
    }


    public Livro encontrarLivroDisponivelParaAlugar(Long livroId) {
        return livroRepository.findByIdAndEstaLocadoFalse(livroId)
          .orElseThrow(() -> new EntityNotFoundException("Livro com id " +  livroId  +
                                                         " não disponível ou não foi encontrado"));
    }


    public Page<LivroResponseDto> listarLivrosDisponiveis(Pageable pageable) {
        return livroRepository.findAllByEstaLocado(pageable, false).map(livroMapper::toResponseDto);
    }

    public Page<LivroResponseDto> listarLivrosIndisponiveis(Pageable pageable) {
        return livroRepository.findAllByEstaLocado(pageable, true).map(livroMapper::toResponseDto);

    }

    public LivroResponseDto encontrarLivroPorId(Long id) {
        return livroMapper.toResponseDto(finderLivroPorId(id));
    }

    @Transactional
    public void deletarPorId(Long id) {
        Livro livro = finderLivroPorId(id);
        if (livro.isEstaLocado()){
            throw new BusinessException("O livro não pode ser exlcuído");
        }
        for (Autor autor : livro.getAutores()) {
            autor.getLivros().remove(livro);
        }
        livroRepository.delete(livro);
    }
}
