package dutkercz.biblioteca.service.validacoes;

import dutkercz.biblioteca.exception.custom.BusinessException;
import dutkercz.biblioteca.model.Autor;
import dutkercz.biblioteca.repository.AutorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ValidarListaDeAutores {

    private final AutorRepository autorRepository;

    public List<Autor> validarIds(List<Long> autoresIds){
        if (autoresIds == null || autoresIds.isEmpty()) throw new BusinessException("Autores não informados no cadastro");
        return autoresIds
                .stream()
                .map(
        id -> autorRepository.findById(id)
                    .orElseThrow(() ->
                             new EntityNotFoundException("Autor com id " + id + " não encontrado")))
                .collect(Collectors.toList());
    }
}
