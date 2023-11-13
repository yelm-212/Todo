package com.codestates.yelm212.Todo.config.token;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
//@Entity(name = "refreshtoken")
@RedisHash(value = "refreshToken", timeToLive = 720)
public class RefreshToken {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", updatable = false)
    private Long id;

//    @Column(name = "member_id", nullable = false, unique = true)
    private Long memberId;

//    @Column(name = "refresh_token", nullable = false)
    @Indexed
    private String refreshToken;

    public RefreshToken(Long memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
