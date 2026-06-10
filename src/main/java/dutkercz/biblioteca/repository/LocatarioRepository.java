package dutkercz.biblioteca.repository;

import dutkercz.biblioteca.model.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocatarioRepository extends JpaRepository<Locatario, Long> {
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
