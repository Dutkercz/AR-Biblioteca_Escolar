package dutkercz.biblioteca.repository;

import dutkercz.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro,Long> {
    Optional<Livro> findByIdAndEstaLocadoFalse(Long livroId);
}
