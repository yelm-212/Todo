package com.codestates.yelm212.Todo.slice.mock;


import com.codestates.yelm212.Todo.todo.dto.TodoDto;
import com.codestates.yelm212.Todo.todo.entity.TodoEntity;
import com.codestates.yelm212.Todo.todo.mapper.TodoMapper;
import com.codestates.yelm212.Todo.todo.service.TodoService;
import com.google.gson.Gson;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerMockTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TodoService todoService;

    @Autowired
    private TodoMapper mapper;

    @Test
    void postTodoTest() throws Exception {
        TodoDto.Post post = new TodoDto.Post(
                "Spring 공부하기", 1L, false);

        TodoEntity todo = mapper.todoPostToTodo(post);
        todo.setId(1L);

        given(todoService.createTodo(Mockito.any())).willReturn(todo);

        String content = gson.toJson(post);


        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/"))));
    }
}
