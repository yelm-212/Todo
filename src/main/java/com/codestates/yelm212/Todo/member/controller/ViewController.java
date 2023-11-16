package com.codestates.yelm212.Todo.member.controller;

import com.codestates.yelm212.Todo.config.token.TokenService;
import com.codestates.yelm212.Todo.member.dto.LoginDto;
import com.codestates.yelm212.Todo.member.dto.MemberDto;
import com.codestates.yelm212.Todo.member.entity.Member;
import com.codestates.yelm212.Todo.member.service.MemberService;
import com.codestates.yelm212.Todo.member.service.UserDetailService;
import com.codestates.yelm212.Todo.utils.UriCreator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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

    @GetMapping("/members/login")
    public String login() {
        return "deflogin.html";
    }

    @GetMapping("/oauth2/login")
    public String oauth2Login() {
        return "oauthLogin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public ResponseEntity postMember(@RequestBody MemberDto.SignUp requestBody) {
        Member createdMember = memberService.createMember(requestBody);
        URI location = UriCreator.createUri(MEMBER_DEF_URL, createdMember.getMemberId());
        return ResponseEntity.created(location).build();
    }

    @Transactional
    @PostMapping("/api/login")
    public ResponseEntity loginMember(@RequestBody LoginDto.Login requestBody) {

        Member loadedMember = userDetailService.loadUserByUsername(requestBody.getEmail());
        String token = tokenService.createRefreshTokenByMember(loadedMember);
        String accessToken = tokenService.createAccessToken(loadedMember);
        HttpHeaders headers = new HttpHeaders();
        headers.add("RefreshToken", token);
        headers.add("Authorization", accessToken);

        return  ResponseEntity.accepted()
                .headers(headers)
                .body(loadedMember.getName());
    }

    @PostMapping ("/api/logout")
    public ResponseEntity logoutMember(@RequestHeader("RefreshToken") String refreshToken) {
        tokenService.deleteRefreshTokenByRefreshToken(refreshToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping ("/api/reissue")
    public ResponseEntity reissue(@RequestHeader("RefreshToken") String refreshToken) {
        tokenService.reissueRefreshToken(refreshToken);
        return ResponseEntity.ok().build();
    }

}