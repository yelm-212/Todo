package com.codestates.yelm212.Todo.helper;

import com.codestates.yelm212.Todo.todo.dto.TodoDto;
import com.codestates.yelm212.Todo.todo.entity.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StubData {

    private static Map<HttpMethod, Object> stubRequestBody;
    static {
        stubRequestBody = new HashMap<>();
        stubRequestBody.put(HttpMethod.POST, new TodoDto.Post("공부하기", 1L, false));
        stubRequestBody.put(HttpMethod.PATCH, new TodoDto.Patch(1L, "Spring 공부하기", 2L, false));
    }

    public static class MockTodo {
        public static Object getRequestBody(HttpMethod method) {
            return stubRequestBody.get(method);
        }

        public static TodoEntity getSingleResponseBody() {
            TodoEntity todo = new TodoEntity("공부하기", 1L, false);
            return todo;
        }

        public static Page<TodoEntity> getMultiResponseBody() {
            TodoEntity todo1 = new TodoEntity("공부하기", 1L, false);

            TodoEntity todo2 = new TodoEntity("운동하기", 2L, false);
            return new PageImpl<>(List.of(todo1, todo2),
                    PageRequest.of(0, 10, Sort.by("memberId").descending()),
                    2);
        }

        public static TodoEntity getSingleResponseBody(long todoId) {
            TodoEntity todo1 = new TodoEntity("공부하기", 1L, false);
            todo1.setId(todoId);
            return todo1;
        }

//        public static TodoEntity getSingleResponseBody(long todoId, Map<String, String> updatedInfo) {
//            String name = Optional.ofNullable(updatedInfo.get("title")).orElse("홍길동");
//            String phone = Optional.ofNullable(updatedInfo.get("phone")).orElse("010-1111-1111");
//            TodoEntity todo1 = new TodoEntity("공부하기", 1L, false);
//            todo1.setId(todoId);
//            return member;
//        }
    }
}
