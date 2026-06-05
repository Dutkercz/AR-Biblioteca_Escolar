CREATE TABLE autor_livro(
    autor_id BIGINT NOT NULL,
    livro_id BIGINT NOT NULL,

    CONSTRAINT pk_autor_livro PRIMARY KEY (autor_id, livro_id),
    CONSTRAINT fk_autor_livro__autor FOREIGN KEY (autor_id) REFERENCES autores(id),
    CONSTRAINT fk_autor_livro__livro FOREIGN KEY (livro_id) REFERENCES livros(id)
);