CREATE TABLE alugueis(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    data_retirada DATE ,
    data_devolucao DATE,
    status ENUM('ATIVO','FINALIZADO','CANCELADO') NOT NULL,
    locatario_id BIGINT,

    CONSTRAINT fk_aluguel_locatario FOREIGN KEY (locatario_id) REFERENCES locatarios(id)
);

CREATE TABLE aluguel_livro(
    aluguel_id BIGINT NOT NULL ,
    livro_id BIGINT NOT NULL ,

    CONSTRAINT pk_aluguel_livro PRIMARY KEY (aluguel_id, livro_id),
    CONSTRAINT fk_aluguel_livro__aluguel FOREIGN KEY (aluguel_id) REFERENCES alugueis(id),
    CONSTRAINT fk_aluguel_livro__livro FOREIGN KEY (livro_id) REFERENCES livros(id)
);