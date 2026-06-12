package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.aluguel.AluguelRequestDto;
import dutkercz.biblioteca.dto.aluguel.AluguelResponseDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.exception.custom.BusinessException;
import dutkercz.biblioteca.mapper.AluguelMapper;
import dutkercz.biblioteca.mapper.LivroMapper;
import dutkercz.biblioteca.domain.Aluguel;
import dutkercz.biblioteca.domain.Livro;
import dutkercz.biblioteca.domain.Locatario;
import dutkercz.biblioteca.domain.enums.AluguelStatus;
import dutkercz.biblioteca.repository.AluguelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AluguelServiceTest {

    @InjectMocks
    private AluguelService aluguelService;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private AluguelMapper aluguelMapper;

    @Mock
    private LivroService livroService;

    @Mock
    private LocatarioService locatarioService;

    @Mock
    private LivroMapper livroMapper;

    @Test
    @DisplayName("Deve alugar livros com sucesso")
    void deveAlugarLivrosComSucesso() {
        var livrosIds = List.of(1L, 2L);
        var request = new AluguelRequestDto(livrosIds, 20L, LocalDate.now().plusDays(2));

        var locatario = new Locatario();
        var livro1 = new Livro();
        var livro2 = new Livro();
        var aluguel = new Aluguel();
        var response = mock(AluguelResponseDto.class);

        when(locatarioService.locatarioFinder(20L)).thenReturn(locatario);
        when(livroService.encontrarLivroDisponivelParaAlugar(1L)).thenReturn(livro1);
        when(livroService.encontrarLivroDisponivelParaAlugar(2L)).thenReturn(livro2);
        when(aluguelMapper.toEntity(request, locatario, List.of(livro1, livro2)))
                .thenReturn(aluguel);
        when(aluguelRepository.save(aluguel)).thenReturn(aluguel);
        when(aluguelMapper.toResponseDto(aluguel)).thenReturn(response);

        var resultado = aluguelService.alugarLivro(request);

        assertEquals(response, resultado);
        assertTrue(livro1.isEstaLocado());
        assertTrue(livro2.isEstaLocado());
        verify(aluguelRepository).save(aluguel);
    }

    @Test
    @DisplayName("Deve finalizar um aluguel ATIVO com sucesso")
    void deveFinalizarAluguel() {
        var livro = new Livro();
        var aluguel = new Aluguel();
        livro.setEstaLocado(true);
        aluguel.setLivros(List.of(livro));
        aluguel.setStatus(AluguelStatus.ATIVO);

        var response = mock(AluguelResponseDto.class);

        when(aluguelRepository.findById(1L)).thenReturn(Optional.of(aluguel));
        when(aluguelMapper.toResponseDto(aluguel)).thenReturn(response);

        var resultado = aluguelService.finalizarAluguel(1L);

        assertEquals(response, resultado);
        assertEquals(AluguelStatus.FINALIZADO, aluguel.getStatus());
        assertFalse(livro.isEstaLocado());
        assertNotNull(aluguel.getDataDevolucao());
    }

    @Test
    @DisplayName("Deve lançar exception quando ID não existir")
    void deveLancarExceptQuandoAluguelNaoExistir() {
        long idInexistente = 99L;
        when(aluguelRepository.findById(idInexistente)).thenReturn(Optional.empty());

        var result =  assertThrows(EntityNotFoundException.class,
                     () -> aluguelService.aluguelFinder(idInexistente));
        assertEquals("Aluguel de id " + idInexistente+ " não encontrado", result.getMessage());

    }

    @Test
    void deveDeletarAluguelAtivo() {
        var livro = new Livro();
        var aluguel = new Aluguel();
        livro.setEstaLocado(true);
        aluguel.setStatus(AluguelStatus.ATIVO);
        aluguel.setLivros(List.of(livro));

        when(aluguelRepository.findById(1L)).thenReturn(Optional.of(aluguel));

        aluguelService.deletarAluguel(1L);

        assertFalse(livro.isEstaLocado());
        verify(aluguelRepository).delete(aluguel);
    }

    @Test
    void naoDeveDeletarAluguelFinalizado() {
        var aluguel = new Aluguel();
        aluguel.setStatus(AluguelStatus.FINALIZADO);

        when(aluguelRepository.findById(1L)).thenReturn(Optional.of(aluguel));

        var result = assertThrows(BusinessException.class,
                                  () -> aluguelService.deletarAluguel(1L));
        assertEquals("Este aluguel não pode ser cancelado", result.getMessage());
        verify(aluguelRepository, never()).delete(any());
    }

    @Test
    void deveRetornarHistoricoSemDuplicidades() {
        var livro = new Livro();
        var aluguel1 = new Aluguel();
        var aluguel2 = new Aluguel();

        aluguel1.setLivros(List.of(livro));
        aluguel2.setLivros(List.of(livro));

        LivroResponseDto dto = mock(LivroResponseDto.class);

        when(locatarioService.locatarioFinder(1L)).thenReturn(new Locatario());
        when(aluguelRepository.findByLocatarioId(1L))
                .thenReturn(List.of(aluguel1, aluguel2));
        when(livroMapper.toResponseDto(livro)).thenReturn(dto);

        Set<LivroResponseDto> resultado = aluguelService.historicoDeLivrosLocadosPorLocatario(1L);

        assertEquals(1, resultado.size());
        verify(livroMapper, times(1)).toResponseDto(livro);
    }
}
