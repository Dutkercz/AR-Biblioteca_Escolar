package dutkercz.biblioteca.model;

import dutkercz.biblioteca.model.enums.GeneroEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locatarios")
public class Locatario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true)
    private String cpf;
}
