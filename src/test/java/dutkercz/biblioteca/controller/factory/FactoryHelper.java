package dutkercz.biblioteca.controller.factory;

import dutkercz.biblioteca.dto.aluguel.AluguelRequestDto;
import dutkercz.biblioteca.dto.autor.AutorRequestDto;
import dutkercz.biblioteca.dto.livro.LivroRequestDto;
import dutkercz.biblioteca.dto.locatario.LocatarioRequestDto;
import dutkercz.biblioteca.mapper.*;
import dutkercz.biblioteca.model.Aluguel;
import dutkercz.biblioteca.model.Autor;
import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.model.Locatario;
import dutkercz.biblioteca.model.enums.AluguelStatus;
import dutkercz.biblioteca.model.enums.GeneroEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class FactoryHelper {

    private static AluguelMapper aluguelMapper = new AluguelMapperImpl();
    private static LocatarioMapper locatarioMapper = new LocatarioMapperImpl();
    private static LivroMapper livroMapper = new LivroMapperImpl();
    private static AutorMapper autorMapper = new AutorMapperImpl();

    public static AluguelRequestDto createAluguelRequestDto(Long locatarioId, Long livroId) {
       return new AluguelRequestDto(
                    List.of(livroId), locatarioId,
                LocalDate.now().plusDays(2));
    }

    public static LocatarioRequestDto createLocatarioRequestDto(){
        return new LocatarioRequestDto("Locatario", GeneroEnum.FEMININO, "55999009988",
                        "email@email.com",LocalDate.of(2000, 1, 1),
                        "12453857045");
    }

    public static LivroRequestDto createLivroRequestDto(){
        Random random = new Random();
        int finalISBN = random.nextInt(100,1000);
        return new LivroRequestDto("Livro1", "1234567891"+finalISBN,
                LocalDate.of(1950, 1,1), List.of(1L));
    }

    public static AutorRequestDto createAutorRequestDto(){
        return new AutorRequestDto("Autor1", GeneroEnum.NAO_BINARIO,
                LocalDate.of(1950, 1, 1),"12345678912");
    }

    public static Autor createAutor(){
        return autorMapper.toEntity(createAutorRequestDto());
    }

    public static Livro createLivro(){
        return livroMapper.toEntity(createLivroRequestDto());
    }

    public static Locatario createLocatario(){
       return locatarioMapper.toEntity(createLocatarioRequestDto());
    }

    public static Aluguel createAluguel(Locatario locatario, List<Livro> livros) {
        var aluguel = aluguelMapper.toEntity(
                createAluguelRequestDto(locatario.getId(),livros.get(0).getId()),
                                             locatario, livros);
        aluguel.setLocatario(locatario);
        aluguel.setLivros(livros);
        return aluguel;
    }

    public static List<Aluguel> listaAluguel(Locatario locatario, List<Livro> livros) {
        var aluguelAtivo = createAluguel(locatario, livros);
        aluguelAtivo.setStatus(AluguelStatus.ATIVO);

        var aluguelFinalizado = createAluguel(locatario, livros);
        aluguelFinalizado.setStatus(AluguelStatus.FINALIZADO);

        var aluguelCancelado = createAluguel(locatario, livros);
        aluguelCancelado.setStatus(AluguelStatus.CANCELADO);

        return List.of(aluguelAtivo, aluguelFinalizado, aluguelCancelado);
    }

    public static List<Livro> listaLivros(){
        var livroDisponivel = createLivro();
        livroDisponivel.setEstaLocado(false);

        var livroAlugado = createLivro();
        livroAlugado.setEstaLocado(true);

        return List.of(livroDisponivel, livroAlugado);
    }
}
