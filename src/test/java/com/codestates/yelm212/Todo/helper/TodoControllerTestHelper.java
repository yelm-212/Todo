package com.codestates.yelm212.Todo.helper;

import java.net.URI;

public interface TodoControllerTestHelper extends ControllerTestHelper{

    String TODO_URL = "/";
    default URI getURI() {
        return createURI(TODO_URL);
    }

    default URI getURI(long todoId) {
        return createURI(TODO_URL + "/{todoId}", todoId);
    }
}
