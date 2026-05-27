# ✅ Desafio Junior Backend — TodoList API

API REST simples para gerenciamento de tarefas, desenvolvida com **Java**, **Spring Boot**, **Spring MVC**, **Spring Data JPA**, **H2**, **MySQL**, **Swagger** e testes automatizados com **JUnit 5** e **WebTestClient**.

---

## 🚀 Funcionalidades

- Criar tarefas
- Listar tarefas
- Atualizar tarefas
- Remover tarefas
- Validar campos obrigatórios
- Retornar erros tratados
- Documentar endpoints com Swagger
- Executar testes de integração

---

## 🧰 Tecnologias

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Jakarta Validation
- H2 Database
- MySQL
- Swagger / OpenAPI
- JUnit 5
- WebTestClient
- Maven

---

## 📁 Estrutura

```text
src
├─ main
│  ├─ java/br/com/raonidev/desafio_todolist
│  │  ├─ controller
│  │  ├─ entity
│  │  ├─ exception
│  │  ├─ repository
│  │  ├─ service
│  │  └─ DesafioTodolistApplication.java
│  └─ resources
│     └─ application.properties
└─ test
   ├─ java/br/com/raonidev/desafio_todolist
   │  └─ DesafioTodolistApplicationTests.java
   └─ resources
      ├─ application.properties
      └─ remove.sql
````

---

## 📌 Endpoints

### Criar tarefa

```http
POST /todos
```

```json
{
  "nome": "todo 1",
  "descricao": "desc todo 1",
  "realizado": false,
  "prioridade": 1
}
```

---

### Listar tarefas

```http
GET /todos
```

---

### Atualizar tarefa

```http
PUT /todos
```

```json
{
  "id": 1,
  "nome": "Todo Atualizado",
  "descricao": "Nova descricao",
  "realizado": true,
  "prioridade": 2
}
```

---

### Remover tarefa

```http
DELETE /todos/{id}
```

---

## 🧯 Tratamento de Erros

A API possui tratamento global de exceções com `@RestControllerAdvice`, evitando respostas com stacktrace e retornando mensagens amigáveis.

Exemplo:

```json
{
  "timestamp": "2026-05-27T03:48:44.4546109",
  "status": 400,
  "error": "Bad Request",
  "message": "Todo não encontrado. Verifique o ID informado e tente novamente.",
  "path": "/todos"
}
```

---

## 📚 Swagger

Após iniciar a aplicação, acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

Por lá é possível visualizar e testar os endpoints da API.

---

## 🧪 Testes

Os testes utilizam **JUnit 5** e **WebTestClient**, com a aplicação subindo em porta aleatória:

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
```

A base de teste é limpa com:

```sql
TRUNCATE TABLE todos;
```

Arquivo:

```text
src/test/resources/remove.sql
```

---

## ▶️ Como executar

### Windows

```bash
mvnw.cmd spring-boot:run
```

### Linux/macOS

```bash
./mvnw spring-boot:run
```

A aplicação será iniciada em:

```text
http://localhost:8080
```

---

## ✅ Rodar testes

```bash
mvn test
```

ou:

```bash
./mvnw test
```

---

## 👨‍💻 Autor

Desenvolvido por **Raoni Rodrigues**.

Projeto criado para praticar backend com Java, Spring Boot, testes automatizados e boas práticas de API REST.

---

## 🏁 Resumo

Uma API pequena, direta e bem estruturada para praticar o essencial do backend:

```text
REST + validação + persistência + testes + Swagger + tratamento de erros
```

Arroz com feijão bem feito. E é daí que nasce backend decente.

```
```
