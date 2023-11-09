package com.codestates.yelm212.Todo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    public class SignUp {
        @NotBlank
        @Email
        private String email;

        @NotBlank(message = "이름은 공백이 아니어야 합니다.")
        private String name;

        @NotBlank(message = "비밀번호를 입력하세요.")
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public class Patch {
        private long memberId;

        @NotBlank(message = "이름은 공백이 아니어야 합니다")
        private String name;

        private String password;
        public void setMemberId(long memberId) {
            this.memberId = memberId;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        private long memberId;
        private String email;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public class Login {
        @NotBlank
        @Email
        private String email;

        @NotBlank(message = "비밀번호를 입력하세요.")
        private String password;
    }
}
