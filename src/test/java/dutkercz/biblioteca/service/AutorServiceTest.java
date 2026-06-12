package dutkercz.biblioteca.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dutkercz.biblioteca.dto.autor.AutorComLivrosResponseDto;
import dutkercz.biblioteca.dto.autor.AutorRequestDto;
import dutkercz.biblioteca.dto.autor.AutorResponseDto;
import dutkercz.biblioteca.exception.custom.BusinessException;
import dutkercz.biblioteca.mapper.AutorMapper;
import dutkercz.biblioteca.domain.Autor;
import dutkercz.biblioteca.domain.Livro;
import dutkercz.biblioteca.repository.AutorRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    @InjectMocks
    private AutorService autorService;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private AutorMapper autorMapper;

    @Mock
    private LivroService livroService;

    @Test
    void deveCadastrarAutorComSucesso() {
        AutorRequestDto request = mock(AutorRequestDto.class);

        when(request.cpf()).thenReturn("12345678900");

        Autor autor = new Autor();
        AutorResponseDto response = mock(AutorResponseDto.class);

        when(autorRepository.existsByCpf("12345678900")).thenReturn(false);
        when(autorMapper.toEntity(request)).thenReturn(autor);
        when(autorRepository.save(autor)).thenReturn(autor);
        when(autorMapper.ToResponseDto(autor)).thenReturn(response);

        var result = autorService.cadastrarAutor(request);

        assertEquals(response, result);
        verify(autorRepository).save(autor);
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExistir() {
        AutorRequestDto request = mock(AutorRequestDto.class);

        when(request.cpf()).thenReturn("12345678900");
        when(autorRepository.existsByCpf("12345678900")).thenReturn(true);

        var result = assertThrows(EntityExistsException.class,
                () -> autorService.cadastrarAutor(request));

        assertEquals("Autor já cadastrado", result.getMessage());
        verify(autorRepository, never()).save(any());
    }

    @Test
    void deveEncontrarAutoresPorNome() {
        Pageable pageable = PageRequest.of(0, 10);
        Autor autor = new Autor();
        AutorResponseDto dto = mock(AutorResponseDto.class);
        Page<Autor> page = new PageImpl<>(List.of(autor));

        when(autorRepository.findByNomeContainingIgnoreCase("Autor", pageable)).thenReturn(page);
        when(autorMapper.ToResponseDto(autor)).thenReturn(dto);

        Page<AutorResponseDto> resultado = autorService.encontrarAutorPorNome("Autor", pageable);

        assertEquals(1, resultado.getContent().size());
    }

    @Test
    void deveRetornarLivrosDoAutor() {
        Autor autor = new Autor();
        Livro livro = new Livro();
        autor.setLivros(List.of(livro));

        AutorComLivrosResponseDto response = mock(AutorComLivrosResponseDto.class);

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(autorMapper.ToResponseComLivrosDto(autor)).thenReturn(response);

        AutorComLivrosResponseDto resultado = autorService.encontrarLivrosPorAutorId(1L);

        assertEquals(response, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoAutorNaoPossuirLivros() {
        Autor autor = new Autor();
        autor.setLivros(new ArrayList<>());

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

        var result = assertThrows(BusinessException.class,
                     () -> autorService.encontrarLivrosPorAutorId(1L));
        assertEquals("Autor não possui livros cadastrados", result.getMessage());
    }

    @Test
    void deveDeletarAutorSemLivros() {
        Autor autor = new Autor();
        autor.setLivros(new ArrayList<>());

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

        autorService.deletarPorId(1L);

        verify(autorRepository).delete(autor);
    }

    @Test
    void naoDeveDeletarAutorComLivros() {
        Autor autor = new Autor();
        Livro livro = new Livro();
        autor.setLivros(List.of(livro));

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

        var result = assertThrows(BusinessException.class,
                () -> autorService.deletarPorId(1L));

        assertEquals("Autor possui livros cadastrados, e não pode ser excluído", result.getMessage());
        verify(autorRepository, never()).delete(any());
    }

    @Test
    void deveRemoverAutoriaDeLivro() {
        Autor autor = new Autor();
        Livro livro = new Livro();
        autor.setLivros(new ArrayList<>(List.of(livro)));
        livro.setAutores(new ArrayList<>(List.of(autor)));

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(livroService.finderLivroPorId(10L)).thenReturn(livro);

        autorService.removerAutoriaDeLivro(1L, 10L);

        assertFalse(autor.getLivros().contains(livro));
        assertFalse(livro.getAutores().contains(autor));
    }

    @Test
    void deveLancarExcecaoQuandoAutorNaoEstiverAssociadoAoLivro() {
        Autor autor = new Autor();
        Livro livro = new Livro();
        livro.setAutores(new ArrayList<>());

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(livroService.finderLivroPorId(10L)).thenReturn(livro);

        var result = assertThrows(BusinessException.class,
                () -> autorService.removerAutoriaDeLivro(1L, 10L));

        assertEquals("Autor não está associado a este livro", result.getMessage());
    }

    @Test
    void deveExcluirLivroQuandoNaoRestaremAutores() {
        Autor autor = new Autor();
        Livro livro = new Livro();
        autor.setLivros(new ArrayList<>(List.of(livro)));
        livro.setAutores(new ArrayList<>(List.of(autor)));

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(livroService.finderLivroPorId(10L)).thenReturn(livro);

        autorService.removerAutoriaDeLivro(1L, 10L);

        verify(livroService).deletarPorId(10L);
    }

    @Test
    void deveLancarExcecaoQuandoAutorNaoExistir() {
        Long idInexistente = 10L;
        when(autorRepository.findById(idInexistente)).thenReturn(Optional.empty());

        var result = assertThrows(EntityNotFoundException.class,
                () -> autorService.deletarPorId(idInexistente));

        assertEquals("Autor de id " + idInexistente + " não encontrado", result.getMessage());
    }
}
