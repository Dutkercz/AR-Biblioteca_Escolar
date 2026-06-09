package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.autor.AutorComLivrosResponseDto;
import dutkercz.biblioteca.dto.autor.AutorRequestDto;
import dutkercz.biblioteca.dto.autor.AutorResponseDto;
import dutkercz.biblioteca.exception.custom.BusinessException;
import dutkercz.biblioteca.mapper.AutorMapper;
import dutkercz.biblioteca.model.Autor;
import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.repository.AutorRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;
    private final LivroService livroService;

    private boolean existeAutorPorCpf(String cpf) {
        return autorRepository.existsByCpf(cpf);
    }
    private Autor finderAutorPorId(Long id) {
        return autorRepository.findById(id).orElseThrow( () ->
                  new EntityNotFoundException("Autor de id "+id+" não encontrado"));
    }

    public AutorResponseDto cadastrarAutor(@Valid AutorRequestDto requestDto) {
        if (existeAutorPorCpf(requestDto.cpf())){
            throw new EntityExistsException("Autor já cadastrado");
        }
        Autor autor = autorRepository.save(autorMapper.toEntity(requestDto));
        return autorMapper.ToResponseDto(autor);
    }

    public Page<AutorResponseDto> encontrarAutorPorNome(String nome,  Pageable pageable) {
        return autorRepository.findByNomeContainingIgnoreCase(nome,pageable).map(autorMapper::ToResponseDto);
    }

    public AutorComLivrosResponseDto encontrarLivrosPorAutorId(Long id) {
        Autor autor = finderAutorPorId(id);
        if(autor.getLivros().isEmpty()){
            throw new BusinessException("Autor não possui livros cadastrados");
        }
        return autorMapper.ToResponseComLivrosDto(autor);
    }

    public void deletarPorId(Long id) {
        Autor autor = finderAutorPorId(id);
        if (!autor.getLivros().isEmpty()){
            throw new BusinessException("Autor possui livros cadastrados, e não pode ser excluído");
        }
        autorRepository.delete(autor);
    }

    @Transactional
    public void removerAutoriaDeLivro(Long id, Long livroId) {
        Autor autor = finderAutorPorId(id);
        Livro livro = livroService.finderLivroPorId(livroId);
        if (!livro.getAutores().contains(autor)) {
            throw new BusinessException("Autor não está associado a este livro");
        }
        livro.removeAutor(autor);
        autor.removeLivro(livro);
        if (livro.getAutores().isEmpty()) {
            livroService.deletarPorId(livroId);
        }
    }
}
