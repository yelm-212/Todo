package com.codestates.yelm212.Todo.todo.contoller;

import com.codestates.yelm212.Todo.todo.service.TodoService;
import com.codestates.yelm212.Todo.utils.UriCreator;
import com.codestates.yelm212.Todo.dto.ListResponseDto;
import com.codestates.yelm212.Todo.dto.PageResponseDto;
import com.codestates.yelm212.Todo.dto.SingleResponseDto;
import com.codestates.yelm212.Todo.todo.dto.TodoDto;
import com.codestates.yelm212.Todo.todo.entity.TodoEntity;
import com.codestates.yelm212.Todo.todo.mapper.TodoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/")
@Validated
@Slf4j
public class TodoController {
    private final static String TODO_DEF_URL = "/";
    private final TodoMapper todoMapper;
    private final TodoService todoService;

    public TodoController(TodoMapper todoMapper, TodoService todoService) {
        this.todoMapper = todoMapper;
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity postTodo(@Valid @RequestBody TodoDto.Post requestBody){
        TodoEntity todo = todoMapper.todoPostToTodo(requestBody);

        TodoEntity createdTodo = todoService.createTodo(todo);
        URI location = UriCreator.createUri(TODO_DEF_URL, createdTodo.getId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{todo-id}")
    public ResponseEntity patchTodo(@PathVariable("todo-id") @Positive long Id,
                                    @Valid @RequestBody TodoDto.Patch requestBody){
        requestBody.setTodoId(Id);
        TodoEntity todo = todoService.updateTodo(todoMapper.todoPatchToTodo(requestBody));
        return new ResponseEntity<>(
                new SingleResponseDto<>(todoMapper.todoToTodoResponse(todo)),
                        HttpStatus.OK);
    }

    @GetMapping("/{todo-id}")
    public ResponseEntity getTodo(
            @PathVariable("todo-id") @Positive long Id) {
        TodoEntity todo = todoService.findTodo(Id);
        return new ResponseEntity<>(
                new SingleResponseDto<>(todoMapper.todoToTodoResponse(todo))
                , HttpStatus.OK);
    }

    // get todos without pagination
    @GetMapping
    public ResponseEntity getTodos() {
        List<TodoEntity> todos = todoService.findTodos();
        return new ResponseEntity<>(
                new ListResponseDto<>(todoMapper.todosToTodoResponses(todos)),
                HttpStatus.OK);
    }

    // get todos with pagination
    @GetMapping("/page")
    public ResponseEntity getTodos(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {
        Page<TodoEntity> pageTodos = todoService.findTodos(page - 1, size);
        List<TodoEntity> Todos = pageTodos.getContent();
        return new ResponseEntity<>(
                new PageResponseDto<>(todoMapper.todosToTodoResponses(Todos),
                        pageTodos),
                HttpStatus.OK);
    }

    @DeleteMapping("/{todo-id}")
    public ResponseEntity deleteMember(
            @PathVariable("todo-id") @Positive long Id) {
        todoService.deleteTodo(Id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
