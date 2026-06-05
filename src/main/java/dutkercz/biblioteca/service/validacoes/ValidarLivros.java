package dutkercz.biblioteca.service.validacoes;

import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.repository.LivroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ValidarLivros {
    private LivroRepository livroRepository;

    public Livro validarDisponibilidade(Long livroId) {
        return livroRepository.findByIdAndEstaLocadoFalse(livroId)
          .orElseThrow(() -> new EntityNotFoundException("Livro com id " + livroId + " não disponivel"));
    }
}
