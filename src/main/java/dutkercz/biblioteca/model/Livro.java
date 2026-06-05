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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String ISBN;

    @Column(nullable = false)
    private LocalDate dataPublicacao;

    private boolean estaLocado = false;

    @ManyToMany(mappedBy = "livros")
    private List<Autor> autores = new ArrayList<>();

    public void addAutor(Autor autor) {
        autores.add(autor);
        autor.getLivros().add(this);
    }

    public void removeAutor(Autor autor) {
        autores.remove(autor);
        autor.getLivros().remove(this);
    }
}
