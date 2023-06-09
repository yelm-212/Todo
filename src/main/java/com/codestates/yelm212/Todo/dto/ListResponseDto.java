package com.codestates.yelm212.Todo.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class ListResponseDto<T> extends ArrayList<T> {
    public ListResponseDto(Collection<? extends T> collection) {
        super(collection);
    }
}
