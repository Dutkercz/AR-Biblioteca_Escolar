package dutkercz.biblioteca.service;

import dutkercz.biblioteca.dto.autor.AutorRequestDto;
import dutkercz.biblioteca.dto.autor.AutorResponseDto;
import dutkercz.biblioteca.mapper.AutorMapper;
import dutkercz.biblioteca.model.Autor;
import dutkercz.biblioteca.repository.AutorRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    public boolean existeAutor(String cpf) {
        return autorRepository.existsByCpf(cpf);
    }

    public AutorResponseDto cadastrarAutor(@Valid AutorRequestDto requestDto) {
        if (existeAutor(requestDto.cpf())){
            throw new EntityExistsException("Autor já cadastrado");
        }
        Autor autor = autorRepository.save(autorMapper.toEntity(requestDto));
        return autorMapper.ToResponseDto(autor);
    }

    public Page<AutorResponseDto> encontrarAutorPorNome(String nome,  Pageable pageable) {
        return autorRepository.findByNomeContainingIgnoreCase(nome,pageable).map(autorMapper::ToResponseDto);
    }
}
