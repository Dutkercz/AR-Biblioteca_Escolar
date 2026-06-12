package dutkercz.biblioteca.repository;

import dutkercz.biblioteca.domain.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocatarioRepository extends JpaRepository<Locatario, Long> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
