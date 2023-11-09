package com.codestates.yelm212.Todo.member.controller;

import com.codestates.yelm212.Todo.dto.ListResponseDto;
import com.codestates.yelm212.Todo.dto.PageResponseDto;
import com.codestates.yelm212.Todo.dto.SingleResponseDto;
import com.codestates.yelm212.Todo.member.dto.MemberDto;
import com.codestates.yelm212.Todo.member.entity.Member;
import com.codestates.yelm212.Todo.member.mapper.MemberMapper;
import com.codestates.yelm212.Todo.member.service.MemberService;
import com.codestates.yelm212.Todo.utils.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/members")
@Validated
@Slf4j
public class MemberController {
    private final static String MEMBER_DEF_URL = "/members";
    private final MemberService memberService;
    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
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

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(
            @PathVariable("member-id") @Positive long memberId,
            @Valid @RequestBody MemberDto.Patch requestBody) {
        requestBody.setMemberId(memberId);

        Member member =
                memberService.updateMember(mapper.memberPatchToMember(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.memberToMemberResponse(member)),
                HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(
            @PathVariable("member-id") @Positive long memberId) {
        Member member = memberService.findMember(memberId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.memberToMemberResponse(member))
                , HttpStatus.OK);
    }

    // get todos without pagination
    @GetMapping
    public ResponseEntity getMembers() {
        List<Member> members = memberService.findMembers();
        return new ResponseEntity<>(
                new ListResponseDto<>(mapper.membersToMemberResponses(members)),
                HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity getMembers(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {
        Page<Member> pageMembers = memberService.findMembers(page - 1, size);
        List<Member> members = pageMembers.getContent();
        return new ResponseEntity<>(
                new PageResponseDto<>(mapper.membersToMemberResponses(members),
                        pageMembers),
                HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(
            @PathVariable("member-id") @Positive long memberId) {
        memberService.deleteMember(memberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
