package com.codestates.yelm212.Todo.config.token;

import com.codestates.yelm212.Todo.config.jwt.ExpireTime;
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
    public String reissueRefreshToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        RefreshToken findToken = refreshTokenService.findByRefreshToken(refreshToken);
        Member member = memberService.findVerifiedMember(findToken.getMemberId());

        return getGeneratedToken(member);
    }

    private String createNewRefreshToken(Member member) {
        RefreshToken refreshToken = refreshTokenService
                .findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));

        return getGeneratedToken(member);
    }

    public String createRefreshTokenByMember(Member member) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByMember(member);
        if (!optionalRefreshToken.isPresent()){
            // if member & token doesnt exists
            String token = getGeneratedToken(member);
            refreshTokenService.addRefreshToken(member.getMemberId(), token); // returns saved RT
            return token;
        }

        // if token already exists, create new token for this member
        RefreshToken refreshToken = optionalRefreshToken.get();
        String newToken = createNewRefreshToken(member);

        refreshTokenService.updateRefreshToken(
                RefreshToken.builder()
                        .id(refreshToken.getId())
                        .refreshToken(newToken)
                        .memberId(member.getMemberId())
                        .build());

        return newToken;
    }

    private String getGeneratedToken(Member member) {
        return tokenProvider.generateToken(member, ExpireTime.REFRESH_TOKEN_EXPIRE_TIME);
    }

    public void deleteRefreshTokenByRefreshToken(String refreshToken){
        RefreshToken findRefreshToken = refreshTokenService.findByRefreshToken(refreshToken);
        refreshTokenService.deleteRefreshToken(findRefreshToken);
    }

    // Todo : Implement this method
    public String createAccessToken(Member member) {
        return tokenProvider.generateAccessToken(member);
    }
}

