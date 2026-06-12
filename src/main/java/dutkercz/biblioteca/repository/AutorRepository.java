package dutkercz.biblioteca.repository;

import dutkercz.biblioteca.domain.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    boolean existsByCpf(String cpf);

    Page<Autor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
