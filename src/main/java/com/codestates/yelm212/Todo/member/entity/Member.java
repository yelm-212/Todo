package com.codestates.yelm212.Todo.member.entity;


import com.codestates.yelm212.Todo.config.oauth2.AuthProvider;
import com.codestates.yelm212.Todo.config.oauth2.oauth2user.OAuth2UserInfo;
import com.codestates.yelm212.Todo.todo.entity.TodoEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "members")
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // post할때 별도 세팅 안해도 알아서 해줌 (식별자)
    private Long memberId;

    @Column(length = 100)
    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    @OneToMany(mappedBy = "member")
    private List<TodoEntity> todos = new ArrayList<>();

    private String oauth2Id;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    // Todo : needs to be deprecated. Use Builder pattern

    public Member update(OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.oauth2Id = oAuth2UserInfo.getOAuth2Id();

        return this;
    }

    public void setTodos(TodoEntity todo) {
        todos.add(todo);
        if(todo.getMember() != this) todo.setMember(this);
    }

    @Builder
    public Member(Long memberId, String name, String email, String password) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
