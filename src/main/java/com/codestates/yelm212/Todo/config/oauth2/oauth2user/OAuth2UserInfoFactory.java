package com.codestates.yelm212.Todo.config.oauth2.oauth2user;

import com.codestates.yelm212.Todo.config.oauth2.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider authProvider, Map<String, Object> attributes) {
        switch (authProvider) {
            case GOOGLE:
                return new GoogleOAuth2User(attributes);
            case NAVER:
                return new NaverOAuth2User(attributes);
            case KAKAO:
                return new KakaoOAuth2User(attributes);

            default:
                throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}