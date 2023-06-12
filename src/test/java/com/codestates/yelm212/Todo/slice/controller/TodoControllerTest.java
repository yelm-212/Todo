package com.codestates.yelm212.Todo.slice.controller;


import com.codestates.yelm212.Todo.helper.StubData;
import com.codestates.yelm212.Todo.helper.TodoControllerTestHelper;
import com.codestates.yelm212.Todo.todo.dto.TodoDto;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest implements TodoControllerTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private ResultActions postResultActions;
    private TodoDto.Post post;

    @BeforeEach
    public void init() throws Exception {
        // given
        this.post = (TodoDto.Post) StubData.MockTodo.getRequestBody(HttpMethod.POST);
        String content = gson.toJson(post);
        URI uri = getURI();
        this.postResultActions = mockMvc.perform(postRequestBuilder(uri, content));
    }

    @Test
    public void postTodoTest() throws Exception {
        // then
        this.postResultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/"))));
    }

    @Test
    public void patchTodoTest() throws Exception {
        // given
        String location = getResourceLocation();
        TodoDto.Patch patch =
                (TodoDto.Patch) StubData.MockTodo.getRequestBody(HttpMethod.PATCH); 
        String content = gson.toJson(patch);

        // when
        ResultActions actions =
                mockMvc.perform(patchRequestBuilder(location, content));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(patch.getTitle()));
    }

    @Test
    public void getTodoTest() throws Exception {
        // when
        String location = getResourceLocation();

        // then
        mockMvc.perform(getRequestBuilder(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(this.post.getTitle()))
                .andExpect(jsonPath("$.data.todoOrder").value(this.post.getTodoOrder()))
                .andExpect(jsonPath("$.data.completed").value(this.post.getCompleted()));
    }

    @Test
    public void getTodosTest() throws Exception {
        // given

        String content = gson.toJson(
                new TodoDto.Post("명상하기", 4L, false));
        URI uri = getURI();

        mockMvc.perform(postRequestBuilder(uri, content));

        // when
        ResultActions actions = mockMvc.perform(getRequestBuilder("/"));

// then
        MvcResult result = actions
                .andExpect(status().isOk())
                .andReturn();

        JsonArray jsonArray = JsonParser.parseString(result.getResponse().getContentAsString()).getAsJsonArray();

        assertThat(jsonArray.size(), is(2));
    }

    @Test
    public void deleteTodoTest() throws Exception {
        // when
        String location = getResourceLocation();

        // then
        mockMvc.perform(deleteRequestBuilder(location))
                .andExpect(status().isNoContent());
    }

    private String getResourceLocation() {
        String location = this.postResultActions.andReturn().getResponse().getHeader("Location"); // "/1"

        return location;
    }
}
