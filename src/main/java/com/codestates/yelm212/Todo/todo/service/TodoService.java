package com.codestates.yelm212.Todo.todo.service;

import com.codestates.yelm212.Todo.todo.exception.ExceptionCode;
import com.codestates.yelm212.Todo.todo.exception.LogicalException;
import com.codestates.yelm212.Todo.todo.entity.TodoEntity;
import com.codestates.yelm212.Todo.todo.repo.TodoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository, ApplicationEventPublisher publisher) {
        this.todoRepository = todoRepository;
    }

    public TodoEntity createTodo(TodoEntity todo) {
        if (verifyIdExists(todo.getId())) {
            throw new LogicalException(ExceptionCode.TODO_EXISTS);
        }

        TodoEntity createdTodo = todoRepository.save(todo);
        return createdTodo;
    }

    public TodoEntity updateTodo(TodoEntity todo) {
        TodoEntity foundTodo = findVerifiedTodo(todo.getId());

        foundTodo.setTitle(todo.getTitle());
        foundTodo.setTodoOrder(todo.getTodoOrder());
        foundTodo.setCompleted(todo.getCompleted());

        return todoRepository.save(foundTodo);
    }

    public List<TodoEntity> findTodos() {
        return todoRepository.findAll();
    }

    public Page<TodoEntity> findTodos(int pages, int size) {

        return todoRepository.findAll(
                PageRequest.of(pages, size,
                        Sort.by("id").ascending()));
    }
    public void deleteTodo(long id) {
        TodoEntity todo = findVerifiedTodo(id);
        todoRepository.delete(todo);
    }

    @Transactional(readOnly = true)
    public TodoEntity findTodo(long id) {

        return findVerifiedTodo(id);
    }
    private boolean verifyIdExists(Long id) {
        Optional<TodoEntity> todo = todoRepository.findById(id);
        return todo.isPresent();
    }

    @Transactional(readOnly = true)
    private TodoEntity findVerifiedTodo(long id) {
        Optional<TodoEntity> todoEntityOptional = todoRepository.findById(id);
        TodoEntity todo = todoEntityOptional.orElseThrow(() ->
                new LogicalException(ExceptionCode.TODO_NOT_FOUND));
        return todo;
    }
}


