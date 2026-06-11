package dutkercz.biblioteca.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import dutkercz.biblioteca.dto.locatario.LocatarioRequestDto;
import dutkercz.biblioteca.dto.locatario.LocatarioResponseDto;
import dutkercz.biblioteca.exception.custom.BusinessException;
import dutkercz.biblioteca.mapper.LocatarioMapper;
import dutkercz.biblioteca.model.Aluguel;
import dutkercz.biblioteca.model.Locatario;
import dutkercz.biblioteca.model.enums.AluguelStatus;
import dutkercz.biblioteca.repository.AluguelRepository;
import dutkercz.biblioteca.repository.LocatarioRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocatarioServiceTest {

    @InjectMocks
    private LocatarioService locatarioService;

    @Mock
    private LocatarioRepository locatarioRepository;

    @Mock
    private LocatarioMapper locatarioMapper;

    @Mock
    private AluguelRepository aluguelRepository;

    @Test
    void deveCadastrarLocatarioComSucesso() {
        LocatarioRequestDto request = mock(LocatarioRequestDto.class);

        when(request.cpf()).thenReturn("12345678900");
        when(request.email()).thenReturn("teste@email.com");

        Locatario entity = new Locatario();
        LocatarioResponseDto response = mock(LocatarioResponseDto.class);

        when(locatarioRepository.existsByCpf("12345678900")).thenReturn(false);
        when(locatarioRepository.existsByEmail("teste@email.com")).thenReturn(false);
        when(locatarioMapper.toEntity(request)).thenReturn(entity);
        when(locatarioRepository.save(entity)).thenReturn(entity);
        when(locatarioMapper.toResponseDto(entity)).thenReturn(response);

        var result = locatarioService.cadastrarLocatario(request);

        assertEquals(response, result);
        verify(locatarioRepository).save(entity);
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExistir() {
        LocatarioRequestDto request = mock(LocatarioRequestDto.class);

        when(request.cpf()).thenReturn("12345678900");
        when(locatarioRepository.existsByCpf("12345678900")).thenReturn(true);

        var result = assertThrows(EntityExistsException.class,
                () -> locatarioService.cadastrarLocatario(request));

        assertEquals("CPF já cadastrados", result.getMessage());
        verify(locatarioRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {
        LocatarioRequestDto request = mock(LocatarioRequestDto.class);

        when(request.email()).thenReturn("teste@email.com");
        when(locatarioRepository.existsByEmail("teste@email.com")).thenReturn(true);

        var result = assertThrows(BusinessException.class,
                () -> locatarioService.cadastrarLocatario(request));

        assertEquals("Email já cadastrados", result.getMessage());
        verify(locatarioRepository, never()).save(any());
    }

    @Test
    void deveDeletarLocatarioSemAlugueisAtivos() {
        Long id = 1L;
        Locatario locatario = new Locatario();
        Aluguel aluguelFinalizado = new Aluguel();
        aluguelFinalizado.setStatus(AluguelStatus.FINALIZADO);
        aluguelFinalizado.setLocatario(locatario);

        when(locatarioRepository.findById(id)).thenReturn(Optional.of(locatario));
        when(aluguelRepository.findByLocatarioId(id)).thenReturn(
                List.of(aluguelFinalizado));

        locatarioService.deletarPorId(id);

        assertNull(aluguelFinalizado.getLocatario());
        verify(locatarioRepository).delete(locatario);
    }

    @Test
    void naoDeveDeletarLocatarioComAluguelAtivo() {
        Long id = 1L;
        Locatario locatario = new Locatario();
        Aluguel aluguelAtivo = new Aluguel();
        aluguelAtivo.setStatus(AluguelStatus.ATIVO);

        when(locatarioRepository.findById(id)).thenReturn(Optional.of(locatario));

        when(aluguelRepository.findByLocatarioId(id)).thenReturn(List.of(aluguelAtivo));

        var result = assertThrows(BusinessException.class,
                () -> locatarioService.deletarPorId(id));

        assertEquals("Locatario possui alugueis em aberto", result.getMessage());
        verify(locatarioRepository, never()).delete(any());
    }

    @Test
    void deveLancarExcecaoQuandoLocatarioNaoExistir() {
        Long idInexistente = 1L;
        when(locatarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        var result = assertThrows(EntityNotFoundException.class,
                () -> locatarioService.deletarPorId(idInexistente));
        assertEquals("Locatario com id " + idInexistente + " não encontrado", result.getMessage());
    }

    @Test
    void deveEncontrarLocatarioPorId() {
        Locatario locatario = new Locatario();

        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));

        var result = locatarioService.locatarioFinder(1L);
        assertEquals(locatario, result);
    }
}
