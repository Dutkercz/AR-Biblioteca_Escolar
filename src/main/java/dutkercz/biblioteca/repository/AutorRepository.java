package dutkercz.biblioteca.repository;

import dutkercz.biblioteca.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    boolean existsByCpf(String cpf);
}
