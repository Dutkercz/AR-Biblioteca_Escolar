package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.livro.LivroRequestDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.exception.custom.BusinessException;
import dutkercz.biblioteca.mapper.LivroMapper;
import dutkercz.biblioteca.domain.Autor;
import dutkercz.biblioteca.domain.Livro;
import dutkercz.biblioteca.repository.LivroRepository;
import dutkercz.biblioteca.service.validacoes.ValidarAutores;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @InjectMocks
    private LivroService livroService;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private ValidarAutores validarAutores;

    @Mock
    private LivroMapper livroMapper;

    @Test
    void deveCadastrarLivroComSucesso() {
        LivroRequestDto request = mock(LivroRequestDto.class);

        when(request.ISBN()).thenReturn("9781234567890");
        when(request.autoresIds()).thenReturn(List.of(1L, 2L));

        Autor autor1 = new Autor();
        Autor autor2 = new Autor();
        Livro livro = spy(new Livro());
        LivroResponseDto response = mock(LivroResponseDto.class);

        when(livroRepository.existsByISBN("9781234567890")).thenReturn(false);
        when(validarAutores.validarIds(List.of(1L, 2L))).thenReturn(
                List.of(autor1, autor2));
        when(livroMapper.toEntity(request)).thenReturn(livro);
        when(livroMapper.toResponseDto(livro)).thenReturn(response);

        var result = livroService.cadastrarLivro(request);

        assertEquals(response, result);
        verify(livroRepository).save(livro);
        verify(livro, times(2)).addAutor(any());
    }

    @Test
    void deveLancarExcecaoQuandoISBNJaExistir() {
        LivroRequestDto request = mock(LivroRequestDto.class);

        when(request.ISBN()).thenReturn("9781234567890");
        when(livroRepository.existsByISBN("9781234567890")).thenReturn(true);

        var result = assertThrows(EntityExistsException.class,
                     () -> livroService.cadastrarLivro(request));

        assertEquals("ISBN já cadastrado", result.getMessage());
        verify(livroRepository, never()).save(any());
    }

    @Test
    void deveEncontrarLivroPorId() {
        Livro livro = new Livro();
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        var result = livroService.finderLivroPorId(1L);

        assertEquals(livro, result);
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoExistir() {
        Long idInexistente = 99L;
        when(livroRepository.findById(idInexistente)).thenReturn(Optional.empty());

        var result = assertThrows(EntityNotFoundException.class,
                () -> livroService.finderLivroPorId(idInexistente)
                    );
        assertEquals("Livro com id "+ idInexistente + " não encontrado", result.getMessage());
    }

    @Test
    void deveRetornarLivroDisponivelParaAlugar() {
        Livro livro = new Livro();
        when(livroRepository.findByIdAndEstaLocadoFalse(1L)).thenReturn(Optional.of(livro));

        Livro result = livroService.encontrarLivroDisponivelParaAlugar(1L);

        assertEquals(livro, result);
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoEstiverDisponivel() {
        Long idIndisponivel = 99L;
        when(livroRepository.findByIdAndEstaLocadoFalse(idIndisponivel)).thenReturn(Optional.empty());

        var result = assertThrows(EntityNotFoundException.class,
                () -> livroService.encontrarLivroDisponivelParaAlugar(idIndisponivel));

        assertEquals("Livro com id " +  idIndisponivel  + " não disponível ou não foi encontrado",
                     result.getMessage());
    }

    @Test
    void deveListarLivrosDisponiveis() {
        Pageable pageable = PageRequest.of(0, 10);
        Livro livro = new Livro();
        LivroResponseDto dto = mock(LivroResponseDto.class);
        Page<Livro> page = new PageImpl<>(List.of(livro));

        when(livroRepository.findAllByEstaLocado(pageable, false)).thenReturn(page);
        when(livroMapper.toResponseDto(livro)).thenReturn(dto);

        var result = livroService.listarLivrosDisponiveis(pageable);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void deveListarLivrosIndisponiveis() {
        Pageable pageable = PageRequest.of(0, 10);
        Livro livro = new Livro();
        LivroResponseDto dto = mock(LivroResponseDto.class);
        Page<Livro> page = new PageImpl<>(List.of(livro));

        when(livroRepository.findAllByEstaLocado(pageable, true)).thenReturn(page);
        when(livroMapper.toResponseDto(livro)).thenReturn(dto);

        var result = livroService.listarLivrosIndisponiveis(pageable);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void deveRetornarLivroResponseDtoPorId() {
        Livro livro = new Livro();
        LivroResponseDto dto = mock(LivroResponseDto.class);

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(livroMapper.toResponseDto(livro)).thenReturn(dto);

        var result = livroService.encontrarLivroPorId(1L);

        assertEquals(dto, result);
    }

    @Test
    void deveDeletarLivroComSucesso() {
        Livro livro = new Livro();
        livro.setEstaLocado(false);
        Autor autor = new Autor();
        autor.setLivros(new java.util.ArrayList<>(List.of(livro)));
        livro.setAutores(List.of(autor));

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        livroService.deletarPorId(1L);

        assertFalse(autor.getLivros().contains(livro));
        verify(livroRepository).delete(livro);
    }

    @Test
    void naoDeveDeletarLivroLocado() {
        Livro livro = new Livro();
        livro.setEstaLocado(true);
        Long idLocado = 99L;

        when(livroRepository.findById(idLocado)).thenReturn(Optional.of(livro));

        var result = assertThrows(BusinessException.class,
                                  () -> livroService.deletarPorId(idLocado));
        assertEquals("O livro não pode ser exlcuído", result.getMessage());
        verify(livroRepository, never()).delete(any());
    }
}
