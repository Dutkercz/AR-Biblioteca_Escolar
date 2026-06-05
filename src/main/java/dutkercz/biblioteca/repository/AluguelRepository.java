package dutkercz.biblioteca.repository;

import dutkercz.biblioteca.model.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
}
