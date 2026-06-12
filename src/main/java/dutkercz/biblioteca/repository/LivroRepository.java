package dutkercz.biblioteca.repository;

import dutkercz.biblioteca.domain.Livro;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro,Long> {
    Optional<Livro> findByIdAndEstaLocadoFalse(Long livroId);

    Page<Livro> findAllByEstaLocado(Pageable pageable, Boolean estaLocado);

    Boolean existsByISBN(@NotBlank String isbn);
}
