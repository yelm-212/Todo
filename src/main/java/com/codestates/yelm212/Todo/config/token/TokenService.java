package com.codestates.yelm212.Todo.config.token;

import com.codestates.yelm212.Todo.config.jwt.TokenProvider;
import com.codestates.yelm212.Todo.member.entity.Member;
import com.codestates.yelm212.Todo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    // token 재발급
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member member = memberService.findVerifiedMember(memberId);

        return tokenProvider.generateToken(member, Duration.ofHours(2));
    }

    public String createAccessTokenByMember(Member member) {
        String token = tokenProvider.generateToken(member, Duration.ofHours(2));
        refreshTokenService.addRefreshToken(token); // returns saved RT
        return token;
    }
}

