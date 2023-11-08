package com.codestates.yelm212.Todo.member.service;

import com.codestates.yelm212.Todo.member.entity.Member;
import com.codestates.yelm212.Todo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberService;

    @Override
    public Member loadUserByUsername(String email) {
        return memberService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}

