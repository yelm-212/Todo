package com.codestates.yelm212.Todo.member.entity;


import com.codestates.yelm212.Todo.todo.entity.TodoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "MEMBERS")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // post할때 별도 세팅 안해도 알아서 해줌 (식별자)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;


    @OneToMany(mappedBy = "member")
    private List<TodoEntity> todos = new ArrayList<>();

    public void setTodos(TodoEntity todo) {
        todos.add(todo);
        if(todo.getMember() != this) todo.setMember(this);
    }

    public Member(Long memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    public Member(String email) {
        this.email = email;
    }

}
