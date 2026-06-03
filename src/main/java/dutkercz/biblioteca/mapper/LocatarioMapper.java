package dutkercz.biblioteca.mapper;

import dutkercz.biblioteca.dto.LocatarioRequestDto;
import dutkercz.biblioteca.dto.LocatarioResponseDto;
import dutkercz.biblioteca.model.Locatario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocatarioMapper {

    @Mapping(target = "cpf", expression = "java(locatarioRequestDto.cpf().replaceAll(\"[.-]\", \"\"))")
    Locatario toEntity(LocatarioRequestDto locatarioRequestDto);
    LocatarioResponseDto toResponseDto(Locatario locatario);

}
