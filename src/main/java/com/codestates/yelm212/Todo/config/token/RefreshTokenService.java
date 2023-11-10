package com.codestates.yelm212.Todo.config.token;

import com.codestates.yelm212.Todo.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow( () -> new IllegalArgumentException("Unexpected Token"));
    }

    public Optional<RefreshToken> findByMember(Member member) {
        return refreshTokenRepository
                .findByMemberId(member.getMemberId());
    }

    @Transactional
    public RefreshToken addRefreshToken(Long memberId, String refreshToken) {
        RefreshToken token = new RefreshToken(memberId, refreshToken);
        return refreshTokenRepository.save(token);
    }

    public void updateRefreshToken(RefreshToken refreshToken) {
//        RefreshToken token = findByMember(member)
//                .orElseThrow(() ->
//                        new IllegalArgumentException("This member doesn't have own token."));

        refreshTokenRepository.save(refreshToken);
    }
}
