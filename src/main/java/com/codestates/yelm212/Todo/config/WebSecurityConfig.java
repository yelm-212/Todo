package com.codestates.yelm212.Todo.config;

import com.codestates.yelm212.Todo.config.cookies.CookieAuthorizationRequestRepository;
import com.codestates.yelm212.Todo.config.oauth2.oauth2user.CustomOAuth2UserService;
import com.codestates.yelm212.Todo.config.oauth2.OAuth2AuthenticationFailureHandler;
import com.codestates.yelm212.Todo.config.oauth2.OAuth2AuthenticationSuccessHandler;
import com.codestates.yelm212.Todo.member.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailService userService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())
                .antMatchers("/static/**", "/");
    }

    // Todo: Edit requestMatcher, login pages ... as required on APIs
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin()
                .loginPage("/members/login")
                .usernameParameter("email") // 아이디 파라미터
                .passwordParameter("password") // 패스워드 파라미터
                .defaultSuccessUrl("/todos") // 로그인 성공 후 이동 페이지.
                .successHandler(
                        new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
                                System.out.println("authentication : " + authentication.getName());
                                response.sendRedirect("/todos"); // 인증 성공한 후 이동
                            }
                        }
                )
                .failureHandler(
                        new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(
                            HttpServletRequest request,
                            HttpServletResponse response,
                            AuthenticationException exception) throws IOException, ServletException {
                        System.out.println("exception : " + exception.getMessage());
                        response.sendRedirect("/login"); // 실패시 로그인 페이지로 되돌아감
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .and()
                .csrf().disable()
                .rememberMe().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .antMatchers("/favicon.ico",
                        "/members/login", "/signup",
                        "/api/**", "/oauth2/**").permitAll()
                .anyRequest().authenticated();

        http.oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")  // OAuth login url
                .authorizationRequestRepository(cookieAuthorizationRequestRepository)  // save requests as cookie
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")  // redirect url
                .and()

                //userService()는 OAuth2 인증 과정에서 Authentication 생성에 필요한 OAuth2User를 반환하는 클래스를 지정한다.
                .userInfoEndpoint().userService(customOAuth2UserService)  // saves member infos.
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}