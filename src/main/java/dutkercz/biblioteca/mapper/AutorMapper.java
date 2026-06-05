package dutkercz.biblioteca.mapper;

import dutkercz.biblioteca.dto.autor.AutorRequestDto;
import dutkercz.biblioteca.dto.autor.AutorResponseDto;
import dutkercz.biblioteca.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    @Mapping(target = "livros", ignore = true)
    Autor toEntity(AutorRequestDto autorRequestDto);
    AutorResponseDto ToResponseDto(Autor autor);
}
