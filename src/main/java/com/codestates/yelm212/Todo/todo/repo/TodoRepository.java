package com.codestates.yelm212.Todo.todo.repo;

import com.codestates.yelm212.Todo.todo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {
    Optional<TodoEntity> findByTitle(String title);
    Optional<TodoEntity> findById(Long Id);

}
