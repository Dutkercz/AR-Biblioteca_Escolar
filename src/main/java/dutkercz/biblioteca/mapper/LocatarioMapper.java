package dutkercz.biblioteca.mapper;

import dutkercz.biblioteca.dto.locatario.LocatarioRequestDto;
import dutkercz.biblioteca.dto.locatario.LocatarioResponseDto;
import dutkercz.biblioteca.domain.Locatario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocatarioMapper {

    @Mapping(target = "cpf", expression = "java(locatarioRequestDto.cpf().replaceAll(\"[.-]\", \"\"))")
    @Mapping(target = "telefone", expression = "java(locatarioRequestDto.telefone().replaceAll(\"[-]\", \"\"))")
    Locatario toEntity(LocatarioRequestDto locatarioRequestDto);
    LocatarioResponseDto toResponseDto(Locatario locatario);

}
