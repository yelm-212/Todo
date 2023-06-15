package com.codestates.yelm212.Todo.restdocs.todo;

import com.codestates.yelm212.Todo.todo.contoller.TodoController;
import com.codestates.yelm212.Todo.todo.dto.TodoDto;
import com.codestates.yelm212.Todo.todo.entity.TodoEntity;
import com.codestates.yelm212.Todo.todo.mapper.TodoMapper;
import com.codestates.yelm212.Todo.todo.service.TodoService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.codestates.yelm212.Todo.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.codestates.yelm212.Todo.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

@WebMvcTest(TodoController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class TodoControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @MockBean
    private TodoMapper mapper;

    @Autowired
    private Gson gson;

    @Test
    public void postTodoTest() throws Exception{
        // given
        TodoDto.Post post = new TodoDto.Post(
                "공부하기", 1L, false);
        String content = gson.toJson(post);

        TodoDto.Response response = new TodoDto.Response(
                1L, 1L,
                "공부하기,",
                false
        );

        given(mapper.todoPostToTodo(Mockito.any(TodoDto.Post.class)))
                .willReturn(new TodoEntity());

        TodoEntity mockTodo = new TodoEntity();
        mockTodo.setId(1L);
        given(todoService.createTodo(Mockito.any(TodoEntity.class)))
                .willReturn(mockTodo);

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
                .andExpect(header().string("Location", is(startsWith("/"))))
                .andDo(document("post-todo",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING)
                                                .description("할 일 내용"),
                                        fieldWithPath("todoOrder").type(JsonFieldType.NUMBER)
                                                .description("할 일의 우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN)
                                                .description("완료 여부")
                                )
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header. 등록된 리소스의 URI")
                        )
                ));
    }

    @Test
    public void patchTodoTest() throws Exception {
        // given
        TodoDto.Post post = new TodoDto.Post(
                "공부하기", 1L, false);
        String contentTodo = gson.toJson(post);

        TodoDto.Response responseTodo = new TodoDto.Response(
                1L, 1L,
                "공부하기,",
                false
        );


        ResultActions actions =
                mockMvc.perform(
                        post("/")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(contentTodo)
                );

        TodoDto.Patch patch = new TodoDto.Patch(
                1L,
                "Spring 공부하기",
                1L,
                false
        );
        String content = gson.toJson(patch);

        TodoDto.Response response = new TodoDto.Response(
                1L,
                1L,
                "Spring 공부하기",
                false
        );

        given(mapper.todoPatchToTodo(Mockito.any(TodoDto.Patch.class)))
                .willReturn(new TodoEntity());

        // when
        ResultActions patchActions = mockMvc.perform(
                MockMvcRequestBuilders.patch("/{todo-id}", 1L) //
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        patchActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(response.getId()))
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.todoOrder").value(response.getTodoOrder()))
                .andExpect(jsonPath("$.data.completed").value(response.getCompleted()))
                .andDo(document("patch-todo",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Todo ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("Todo title"),
                                fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("Todo order"),
                                fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("Todo completed or not")
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("Todo ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("Todo title"),
                                fieldWithPath("data.todoOrder").type(JsonFieldType.NUMBER).description("Todo order"),
                                fieldWithPath("data.completed").type(JsonFieldType.BOOLEAN).description("Todo completed or not")
                        )
                ));
    }


    @Test
    public void getTodoTest() throws Exception{

    }

    @Test
    public void getTodosTest() throws Exception{}

    @Test
    public void deleteTodosTest() throws Exception{}

}
