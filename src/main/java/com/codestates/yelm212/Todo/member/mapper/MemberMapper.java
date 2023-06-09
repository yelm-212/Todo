package com.codestates.yelm212.Todo.member.mapper;

import com.codestates.yelm212.Todo.member.dto.MemberDto;
import com.codestates.yelm212.Todo.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    Member memberPostToMember(MemberDto.Post requestBody);
    MemberDto.Response memberToMemberResponse(Member member);

    List<MemberDto.Response> membersToMemberResponses(List<Member> members);
    Member memberPatchToMember(MemberDto.Patch requestBody);
}
