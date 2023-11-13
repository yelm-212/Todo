package com.codestates.yelm212.Todo.config.jwt;

import java.time.Duration;

public class ExpireTime {
    public static final Duration ACCESS_TOKEN_EXPIRE_TIME = Duration.ofDays(7);
    public static final Duration REFRESH_TOKEN_EXPIRE_TIME = Duration.ofHours(2);
}
