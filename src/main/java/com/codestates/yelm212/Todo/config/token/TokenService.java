package com.codestates.yelm212.Todo.config.token;

import com.codestates.yelm212.Todo.config.jwt.TokenProvider;
import com.codestates.yelm212.Todo.member.entity.Member;
import com.codestates.yelm212.Todo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    // token 재발급
//    public String createNewAccessToken(String refreshToken) {
//        // 토큰 유효성 검사에 실패하면 예외 발생
//        if(!tokenProvider.validToken(refreshToken)) {
//            throw new IllegalArgumentException("Unexpected token");
//        }
//
//        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
//        Member member = memberService.findVerifiedMember(memberId);
//
//        return tokenProvider.generateToken(member, Duration.ofHours(2));
//    }

    public String createNewAccessToken(Member member) {
        RefreshToken refreshToken = refreshTokenService
                .findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));

        return tokenProvider.generateToken(member, Duration.ofHours(2));
    }

    public String createAccessTokenByMember(Member member) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByMember(member);
        if (!optionalRefreshToken.isPresent()){
            // if member & token doesnt exists
            String token = tokenProvider.generateToken(member, Duration.ofHours(2));
            refreshTokenService.addRefreshToken(member.getMemberId(), token); // returns saved RT
            return token;
        }else{
            // if token already exists, create new token for this member
            RefreshToken refreshToken = optionalRefreshToken.get();
            String newAccessToken = createNewAccessToken(member);
            refreshToken.update(newAccessToken);
            refreshTokenService.updateRefreshToken(refreshToken);
            return newAccessToken;
        }
    }
}

