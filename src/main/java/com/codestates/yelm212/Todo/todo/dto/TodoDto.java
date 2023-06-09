package com.codestates.yelm212.Todo.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class TodoDto {

    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank(message = "할 일은 공백으로 둘 수 없습니다.")
        private String title;

        @NotNull(message = "우선순위는 필수입니다.")
        @Positive(message = "우선순위는 자연수여야 합니다.")
        private Long todoOrder;

//        @Pattern(regexp = "^(true|false)$", message = "완료 여부는 'true' 또는 'false' 여야 합니다.")
@NotNull(message = "완료 여부는 'true' 또는 'false' 여야 합니다.")
private Boolean completed;

    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private Long id;

        @NotBlank(message = "할 일은 공백으로 둘 수 없습니다.")
        private String title;

        @NotNull(message = "우선순위는 필수입니다.")
        @Positive(message = "우선순위는 자연수여야 합니다.")
        private Long todoOrder;

//        @Pattern(regexp = "^(true|false)$", message = "완료 여부는 'true' 또는 'false' 여야 합니다.")
@NotNull(message = "완료 여부는 'true' 또는 'false' 여야 합니다.")
        private String completed;
        public void setTodoId(long id) {
            this.id = id;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long todoOrder;
        private String title;
        private Boolean completed;
    }
}
