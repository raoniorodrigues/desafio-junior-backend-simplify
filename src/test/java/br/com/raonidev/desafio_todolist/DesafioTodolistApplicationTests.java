package br.com.raonidev.desafio_todolist;

import br.com.raonidev.desafio_todolist.entity.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql("/remove.sql")
class DesafioTodolistApplicationTests {

        @Value("${local.server.port}")
        private int port;

        private WebTestClient webTestClient;

        @BeforeEach
        void setup() {
                webTestClient = WebTestClient
                                .bindToServer()
                                .baseUrl("http://localhost:" + port)
                                .build();
        }

        @Test
        void testCreateTodoSuccess() {
                var todo = new Todo("todo 1", "desc todo 1", false, 1);

                webTestClient
                                .post()
                                .uri("/todos")
                                .bodyValue(todo)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$").isArray()
                                .jsonPath("$.length()").isEqualTo(1)
                                .jsonPath("$[0].nome").isEqualTo(todo.getNome())
                                .jsonPath("$[0].descricao").isEqualTo(todo.getDescricao())
                                .jsonPath("$[0].realizado").isEqualTo(todo.getRealizado())
                                .jsonPath("$[0].prioridade").isEqualTo(todo.getPrioridade());
        }

        @Test
        void testCreateTodoFailure() {
                webTestClient
                                .post()
                                .uri("/todos")
                                .bodyValue(
                                                new Todo("", "", false, 1))
                                .exchange()
                                .expectStatus().isBadRequest();
        }

        @Test
        void testListTodosSuccess() {
                // Valida se o endpoint de listagem retorna uma lista vazia quando não há todos
                // cadastrados.
                webTestClient
                                .get()
                                .uri("/todos")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$").isArray()
                                .jsonPath("$.length()").isEqualTo(0);
        }

        @Test
        void testCreateTodoFailureWhenNomeIsEmpty() {
                // Valida que a API não permite criar um todo sem nome.
                var todo = new Todo("", "desc todo 1", false, 1);

                webTestClient
                                .post()
                                .uri("/todos")
                                .bodyValue(todo)
                                .exchange()
                                .expectStatus().isBadRequest();
        }

        @Test
        void testCreateTodoFailureWhenDescricaoIsEmpty() {
                // Valida que a API não permite criar um todo sem descrição.
                var todo = new Todo("todo 1", "", false, 1);

                webTestClient
                                .post()
                                .uri("/todos")
                                .bodyValue(todo)
                                .exchange()
                                .expectStatus().isBadRequest();
        }

        @Test
        void testCreateTwoTodosSuccess() {
                // Valida que a API permite criar mais de um todo e retorna a quantidade
                // atualizada.
                var todo1 = new Todo("todo 1", "desc todo 1", false, 1);
                var todo2 = new Todo("todo 2", "desc todo 2", false, 2);

                webTestClient
                                .post()
                                .uri("/todos")
                                .bodyValue(todo1)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$").isArray()
                                .jsonPath("$.length()").isEqualTo(1);

                webTestClient
                                .post()
                                .uri("/todos")
                                .bodyValue(todo2)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$").isArray()
                                .jsonPath("$.length()").isEqualTo(2);
        }

        @Test
        void testUpdateTodoSuccess() {
                // Cria um todo válido para garantir que exista uma massa real antes da
                // atualização.
                var todo = new Todo("todo 1", "desc todo 1", false, 1);

                webTestClient
                                .post()
                                .uri("/todos")
                                .bodyValue(todo)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$").isArray()
                                .jsonPath("$.length()").isEqualTo(1)
                                .jsonPath("$[0].id").value(id -> {

                                        // Recupera o ID gerado pela própria API, sem fixar valor no teste.
                                        var todoAtualizado = new Todo("todo atualizado", "desc atualizada", true, 2);
                                        todoAtualizado.setId(Long.valueOf(id.toString()));

                                        // Valida se a API atualiza o todo existente usando o endpoint PUT /todos.
                                        webTestClient
                                                        .put()
                                                        .uri("/todos")
                                                        .bodyValue(todoAtualizado)
                                                        .exchange()
                                                        .expectStatus().isOk()
                                                        .expectBody()
                                                        .jsonPath("$").isArray()
                                                        .jsonPath("$.length()").isEqualTo(1)
                                                        .jsonPath("$[0].id").isEqualTo(Integer.valueOf(id.toString()))
                                                        .jsonPath("$[0].nome").isEqualTo(todoAtualizado.getNome())
                                                        .jsonPath("$[0].descricao")
                                                        .isEqualTo(todoAtualizado.getDescricao())
                                                        .jsonPath("$[0].realizado")
                                                        .isEqualTo(todoAtualizado.getRealizado())
                                                        .jsonPath("$[0].prioridade")
                                                        .isEqualTo(todoAtualizado.getPrioridade());
                                });
        }

        @Test
        void testUpdateTodoFailureWhenIdDoesNotExist() {
                // Valida que a API não permite atualizar um todo com ID inexistente.
                var todoAtualizado = new Todo("Todo Atualizado", "Nova descricao", false, 2);
                todoAtualizado.setId(Long.valueOf(3232434));

                webTestClient
                                .put()
                                .uri("/todos")
                                .bodyValue(todoAtualizado)
                                .exchange()
                                .expectStatus().isBadRequest()
                                .expectBody()
                                .jsonPath("$.status").isEqualTo(400)
                                .jsonPath("$.error").isEqualTo("Bad Request")
                                .jsonPath("$.message")
                                .isEqualTo("Todo não encontrado. Verifique o ID informado e tente novamente.")
                                .jsonPath("$.path").isEqualTo("/todos");
        }

        @Test
        void testDeleteTodoSuccess() {
                // Cria um todo válido para garantir que exista um registro antes da exclusão.
                var todo = new Todo("todo 1", "desc todo 1", false, 1);

                webTestClient
                                .post()
                                .uri("/todos")
                                .bodyValue(todo)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$").isArray()
                                .jsonPath("$.length()").isEqualTo(1);

                // Remove o todo criado anteriormente pelo ID gerado após o TRUNCATE.
                webTestClient
                                .delete()
                                .uri("/todos/1")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$").isArray()
                                .jsonPath("$.length()").isEqualTo(0);
        }

        @Test
        void testDeleteTodoFailureWhenIdDoesNotExist() {
                // Valida que a API não permite remover um todo que não existe.
                webTestClient
                                .delete()
                                .uri("/todos/999")
                                .exchange()
                                .expectStatus().isBadRequest();
        }
}