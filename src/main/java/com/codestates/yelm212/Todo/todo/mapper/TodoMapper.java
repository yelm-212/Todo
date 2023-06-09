package com.codestates.yelm212.Todo.todo.mapper;

import com.codestates.yelm212.Todo.todo.dto.TodoDto;
import com.codestates.yelm212.Todo.todo.entity.TodoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TodoMapper {
    TodoEntity todoPostToTodo(TodoDto.Post requestBody);

    TodoEntity todoPatchToTodo(TodoDto.Patch requestBody);

    TodoDto.Response todoToTodoResponse(TodoEntity todo);

    List<TodoDto.Response> todosToTodoResponses(List<TodoEntity> todos);
}
