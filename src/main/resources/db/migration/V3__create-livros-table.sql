CREATE TABLE livros(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome varchar(255) NOT NULL ,
    ISBN VARCHAR(13) UNIQUE NOT NULL ,
    esta_locado BOOLEAN DEFAULT (false),
    data_publicacao DATE NOT NULL
);