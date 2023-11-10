package com.codestates.yelm212.Todo.config.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow( () -> new IllegalArgumentException("Unexpected Token"));
    }

    @Transactional
    public RefreshToken addRefreshToken(String refreshToken) {
        RefreshToken token = findByRefreshToken(refreshToken);
        return refreshTokenRepository.save(token);
    }

    public void deleteRefreshToken(String refreshToken) {
        RefreshToken token = findByRefreshToken(refreshToken);
        refreshTokenRepository.delete(token);
    }
}
