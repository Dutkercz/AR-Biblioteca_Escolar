package dutkercz.biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "alugueis")
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDate dataRetirada = LocalDate.now();

    @Column
    private LocalDate dataDevolucao = LocalDate.now().plusDays(2);

    @ManyToOne
    private Locatario locatario;

    @ManyToMany
    @JoinTable(name = "aluguel_livro",
            joinColumns = @JoinColumn(name = "aluguel_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_id"))
    private List<Livro> livros = new ArrayList<>();
}
