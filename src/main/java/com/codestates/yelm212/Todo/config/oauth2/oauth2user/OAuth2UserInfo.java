package com.codestates.yelm212.Todo.config.oauth2.oauth2user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public abstract String getOAuth2Id();
    public abstract String getEmail();
    public abstract String getName();
}