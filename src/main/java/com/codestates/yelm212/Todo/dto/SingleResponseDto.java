package com.codestates.yelm212.Todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public class SingleResponseDto<T> {
    private T data;
}
