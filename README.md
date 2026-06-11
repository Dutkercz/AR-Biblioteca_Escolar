# Biblioteca-Escolar API

Projeto de API para uma biblioteca escolar que permite o cadastro de Livros, Autores, Locatarios e também a possibilidade de alugar livros.

##  Como Executar o Projeto

Você pode rodar esta aplicação de duas formas: localmente com o Maven ou utilizando o Docker.

###  Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina:
* **Java 21** (ou superior)
* **Maven** (opcional, caso vá rodar localmente)
* **Docker** e **Docker Compose** (caso vá rodar via container)

---

## Rodando com Docker (Recomendado)

Esta é a forma mais rápida, pois já configura a aplicação e o banco de dados automaticamente.

1. Na raiz do projeto (onde está o arquivo `docker-compose.yml`), execute o comando para construir e iniciar os containers:
   ```bash
   docker compose up -d --build
   ```

2. **Acessar a aplicação**  
   Após o término da inicialização a API estará pronta para receber requests em:  
    [http://localhost:8080](http://localhost:8080)
      
   Ou acessar a documentação com Swagger em:  
    [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

3. **Parar a execução**  
   Para encerrar e remover os containers criados pelo Docker Compose, execute:
   ```bash
   docker compose down
   ```

---

## Executando Localmente (Sem Docker)
Caso prefira rodar a aplicação diretamente no seu sistema operacional:

### Passo 1: Configurar o Banco de Dados
Inicialize um banco de dados MySQL na sua máquina.

Abra o arquivo `src/main/resources/application.yml` e ajuste as credenciais de conexão:

```yaml
spring:
  datasource:
    username: seu_usuario
    password: sua_senha
```

### Passo 2: Compilar o projeto
No terminal, na raiz do projeto, execute o comando do Maven para baixar as dependências e buildar o JAR:

```bash
mvn clean install -DskipTests
```
      
### Passo 3: Iniciar a aplicação
Execute o comando abaixo para subir o servidor do Spring Boot:

```bash
mvn spring-boot:run
```
    
### Passo 4: Acessar a aplicação
A API estará pronta para receber requisições em:  
 [http://localhost:8080](http://localhost:8080)
    
Ou acessar a documentação com Swagger em:  
 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Endpoints

### Autor

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/autores` | Cadastrar novo Autor |
| `GET` | `/api/autores?nome=` | Encontrar Autor por nome |
| `GET` | `/api/autores/{id}/livros` | Encontrar livros do Autor |
| `DELETE` | `/api/autores/{id}` | Delete de Autor por ID |
| `DELETE` | `/api/autores/{id}/livro/{livroId}` | Remove o Autor de um livro |

### Livro

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/livros` | Cadastrar novo Livro |
| `GET` | `/api/livros/disponiveis` | Listar livros disponíveis |
| `GET` | `/api/livros/alugados` | Listar livros alugados |
| `GET` | `/api/livros/{id}` | Encontrar livro por ID |
| `DELETE` | `/api/livros/{id}` | Deletar livro por ID |

### Locatario

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/locatarios` | Cadastrar novo Locatário |
| `DELETE` | `/api/locatarios/{id}` | Deletar locatário por ID |

### Aluguel

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/alugueis` | Alugar livro |
| `GET` | `/api/alugueis` | Listar aluguéis |
| `PUT` | `/api/alugueis/devolver/{id}` | Devolver livros |
| `DELETE` | `/api/alugueis/{id}` | Deletar aluguel por ID |
| `GET` | `/api/alugueis/locatario/{id}` | Histórico por locatário |
