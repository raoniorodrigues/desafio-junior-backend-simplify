package br.com.raonidev.desafio_todolist.controller;

import br.com.raonidev.desafio_todolist.entity.Todo;
import br.com.raonidev.desafio_todolist.repository.TodoRepository;
import br.com.raonidev.desafio_todolist.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private TodoService todoService;
    private TodoRepository todoRepository;

    public TodoController(TodoService todoService, TodoRepository todoRepository) {
        this.todoService = todoService;
        this.todoRepository = todoRepository;
    }

    @PostMapping
    List<Todo> create(@RequestBody @Valid Todo todo) {
        return todoService.create(todo);
    }

    @GetMapping
    List<Todo> list() {
        return todoService.list();
    }

    @PutMapping
    List<Todo> update(@RequestBody @Valid Todo todo) {
        if (todo.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do todo é obrigatório para atualização");
        }

        if (!todoRepository.existsById(todo.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo não encontrado");
        }

        return todoService.update(todo);
    }

    @DeleteMapping("{id}")
    List<Todo> delete(@PathVariable("id") Long id) {
        if (!todoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo não encontrado");
        }

        return todoService.delete(id);
    }
}