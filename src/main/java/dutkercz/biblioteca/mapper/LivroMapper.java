package dutkercz.biblioteca.mapper;

import dutkercz.biblioteca.dto.livro.LivroRequestDto;
import dutkercz.biblioteca.dto.livro.LivroResponseDto;
import dutkercz.biblioteca.domain.Livro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    Livro toEntity(LivroRequestDto requestDto);

    @Mapping(target = "autoresNomes", expression = "java(livro.getAutores().stream().map(a -> a.getNome()).toList();)")
    LivroResponseDto toResponseDto(Livro livro);
}
