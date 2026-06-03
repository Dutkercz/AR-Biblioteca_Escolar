package dutkercz.biblioteca.model;

import dutkercz.biblioteca.model.enums.GeneroEnum;
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
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    private GeneroEnum sexo;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true)
    private String cpf;

    @ManyToMany
    @JoinTable(name = "autor_livro",
                joinColumns = @JoinColumn(name = "autor_id"),
                inverseJoinColumns =@JoinColumn (name = "livro_id"))
    List<Livro> livros = new ArrayList<>();
}
