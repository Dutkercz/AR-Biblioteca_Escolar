package dutkercz.biblioteca.mapper;

import dutkercz.biblioteca.dto.aluguel.AluguelRequestDto;
import dutkercz.biblioteca.dto.aluguel.AluguelResponseDto;
import dutkercz.biblioteca.model.Aluguel;
import dutkercz.biblioteca.model.Livro;
import dutkercz.biblioteca.model.Locatario;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AluguelMapper {

    @Mapping(target = "id", ignore = true)
    Aluguel toEntity(AluguelRequestDto requestDto, Locatario locatario, List<Livro> livros);

    AluguelResponseDto toResponseDto(Aluguel aluguel);

//    @Condition
//    default boolean verificaNullos(Object value){
//        if (value instanceof String s){
//            return s != null && !s.trim().isEmpty();
//        }
//        return value != null;
//    }
}
