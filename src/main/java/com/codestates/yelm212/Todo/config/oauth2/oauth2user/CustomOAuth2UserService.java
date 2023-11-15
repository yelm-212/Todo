package com.codestates.yelm212.Todo.config.oauth2.oauth2user;

import com.codestates.yelm212.Todo.config.oauth2.AuthProvider;
import com.codestates.yelm212.Todo.member.entity.Member;
import com.codestates.yelm212.Todo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        //OAuth2 로그인 플랫폼 구분
        AuthProvider authProvider = AuthProvider
                .valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(oAuth2UserInfo.getEmail());

//         이미 가입된 경우
//            해당 메일로 가입한 유저가 이미 존재함
        if (optionalMember.isPresent() && !optionalMember.get().getAuthProvider().equals(authProvider)) {
            throw new RuntimeException("Email already signed up.");
        }
        //  OAuth2에서 가져온 정보를 가지고 기존 유저 정보 업데이트
        if (optionalMember.isPresent()) {
            Member member = updateUser(optionalMember.get(), oAuth2UserInfo);
            return UserPrincipal.create(member, oAuth2UserInfo.getAttributes());
        }

        //가입되지 않은 경우
        Member member = registerUser(authProvider, oAuth2UserInfo);
        return UserPrincipal.create(member, oAuth2UserInfo.getAttributes());
    }

    private Member registerUser(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo) {
        Member member = Member.builder()
                .email(oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getName())
                .oauth2Id(oAuth2UserInfo.getOAuth2Id())
                .authProvider(authProvider)
                .build();
        return memberRepository.save(member);
    }


    private Member updateUser(Member member, OAuth2UserInfo oAuth2UserInfo) {
        return memberRepository.save(member.update(oAuth2UserInfo));
    }
}
