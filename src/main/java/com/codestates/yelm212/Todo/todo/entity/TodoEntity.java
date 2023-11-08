package com.codestates.yelm212.Todo.todo.entity;


import com.codestates.yelm212.Todo.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "todos")
public class TodoEntity {

    @Id // 기본키로 설정하기
    @GeneratedValue(strategy = GenerationType.IDENTITY) // post할때 별도 세팅 안해도 알아서 해줌 (식별자)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Long todoOrder;

    @Column(nullable = false)
    private Boolean completed;

    @Builder
    public TodoEntity(String title, Long todoOrder, Boolean completed) {
        this.title = title;
        this.todoOrder = todoOrder;
        this.completed = completed;
    }

    @ManyToOne
    @JoinColumn(name = "memberId", insertable = false, updatable = false)
    private Member member;

    public void setMember(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}
