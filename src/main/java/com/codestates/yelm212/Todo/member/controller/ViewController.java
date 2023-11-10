package com.codestates.yelm212.Todo.member.controller;

import com.codestates.yelm212.Todo.config.token.TokenDto;
import com.codestates.yelm212.Todo.config.token.TokenService;
import com.codestates.yelm212.Todo.dto.SingleResponseDto;
import com.codestates.yelm212.Todo.member.dto.LoginDto;
import com.codestates.yelm212.Todo.member.dto.MemberDto;
import com.codestates.yelm212.Todo.member.entity.Member;
import com.codestates.yelm212.Todo.member.service.MemberService;
import com.codestates.yelm212.Todo.member.service.UserDetailService;
import com.codestates.yelm212.Todo.utils.UriCreator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.net.URI;

import static com.codestates.yelm212.Todo.member.controller.MemberController.MEMBER_DEF_URL;

@Controller
public class ViewController {

    private final MemberService memberService;
    private final UserDetailService userDetailService;
    private final TokenService tokenService;

    public ViewController(MemberService memberService, UserDetailService userDetailService, TokenService tokenService) {
        this.memberService = memberService;
        this.userDetailService = userDetailService;
        this.tokenService = tokenService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public ResponseEntity postMember(MemberDto.SignUp requestBody) {
        Member createdMember = memberService.createMember(requestBody);
        URI location = UriCreator.createUri(MEMBER_DEF_URL, createdMember.getMemberId());
        return ResponseEntity.created(location).build();
    }

    @Transactional
    @PostMapping("/api/login")
    public ResponseEntity loginMember(LoginDto.Login requestBody) {

        Member loadedMember = userDetailService.loadUserByUsername(requestBody.getEmail());
        String token = tokenService.createAccessTokenByMember(loadedMember);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Refresh", token);

        return  ResponseEntity.accepted()
                .headers(headers)
                .body(new SingleResponseDto<>(loadedMember.getName()));
    }

    @GetMapping("/logout")
    public ResponseEntity logoutMember(HttpServletRequest request, HttpServletResponse response) {
        // Todo: Logout Implementation
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().build();
    }

}