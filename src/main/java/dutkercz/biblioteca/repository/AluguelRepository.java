package dutkercz.biblioteca.repository;

import dutkercz.biblioteca.model.Aluguel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    @Query("""
           SELECT a FROM Aluguel a
           WHERE a.status = 'ATIVO' OR  a.status = 'FINALIZADO'
           """)
    Page<Aluguel> findAllAtivasEFinalizadas(Pageable pageable);
}
