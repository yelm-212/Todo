package com.codestates.yelm212.Todo.todo.exception;

import lombok.Getter;

public class LogicalException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;
    public LogicalException(Object p0) {
    }
}
