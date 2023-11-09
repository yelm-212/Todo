package com.codestates.yelm212.Todo.member.controller;

import com.codestates.yelm212.Todo.member.dto.MemberDto;
import com.codestates.yelm212.Todo.member.entity.Member;
import com.codestates.yelm212.Todo.member.service.MemberService;
import com.codestates.yelm212.Todo.utils.UriCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

import static com.codestates.yelm212.Todo.member.controller.MemberController.MEMBER_DEF_URL;

@Controller
public class ViewController {

    private final MemberService memberService;

    public ViewController(MemberService memberService) {
        this.memberService = memberService;
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

    @PostMapping("/login")
    public ResponseEntity loginMember(MemberDto.Login requestBody) {
//        Member createdMember = memberService.createMember(requestBody);

//        URI location = UriCreator.createUri(MEMBER_DEF_URL, createdMember.getMemberId());
        // Todo: Login Implementation
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity logoutMember(HttpServletRequest request, HttpServletResponse response) {
        // Todo: Logout Implementation
        return ResponseEntity.ok().build();
    }

}