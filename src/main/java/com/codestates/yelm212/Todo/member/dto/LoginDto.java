package com.codestates.yelm212.Todo.member.dto;


import com.nimbusds.oauth2.sdk.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDto {
    @Getter
    @AllArgsConstructor
    public static class Login {
        @NotBlank
        @Email
        private String email;

        @NotBlank(message = "비밀번호를 입력하세요.")
        private String password;
    }

}
