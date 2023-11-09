package com.codestates.yelm212.Todo.member.service;

import com.codestates.yelm212.Todo.member.dto.MemberDto;
import com.codestates.yelm212.Todo.member.entity.Member;
import com.codestates.yelm212.Todo.member.repository.MemberRepository;
import com.codestates.yelm212.Todo.todo.exception.ExceptionCode;
import com.codestates.yelm212.Todo.todo.exception.LogicalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member createMember(MemberDto.SignUp requestBody) {
        verifyExistsEmail(requestBody.getEmail());

        return memberRepository.save(
                Member.builder()
                        .name(requestBody.getName())
                        .email(requestBody.getEmail())
                        .password(bCryptPasswordEncoder.encode(requestBody.getPassword()))
                .build());
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new LogicalException(ExceptionCode.MEMBER_EXISTS);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Member updateMember(Member member){
        Member findMember = findVerifiedMember(member.getMemberId());

        Optional.ofNullable(member.getName())
                .ifPresent(name -> findMember.setName(name));

        return memberRepository.save(findMember);
    }

    @Transactional(readOnly = true)
    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        Member findMember =
                optionalMember.orElseThrow(() ->
                        new LogicalException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }

    public void deleteMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);
        memberRepository.delete(findMember);
    }

    @Transactional(readOnly = true)
    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public Page<Member> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }


    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

}
