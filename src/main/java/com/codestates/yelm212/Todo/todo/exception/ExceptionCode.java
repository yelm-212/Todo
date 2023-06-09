package com.codestates.yelm212.Todo.todo.exception;

import lombok.Getter;

public enum ExceptionCode {
    TODO_NOT_FOUND(404, "Todo not found"),
    TODO_EXISTS(409, "Todo already Exists"),
    MEMBER_EXISTS( 409, "Member Already Exists"),
    MEMBER_NOT_FOUND(404, "Member not found");

    @Getter
    private int stat;
    @Getter
    private String msg;

    ExceptionCode(int stat, String msg) {
        this.stat = stat;
        this.msg = msg;
    }
}
