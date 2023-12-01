package com.eastshine.looknshop.config;

import com.eastshine.looknshop.config.jwt.JwtTokenFilter;
import com.eastshine.looknshop.config.jwt.JwtTokenProvider;
import com.eastshine.looknshop.config.oauth2.OAuth2AuthenticationFailureHandler;
import com.eastshine.looknshop.config.oauth2.OAuth2AuthenticationSuccessHandler;
import com.eastshine.looknshop.repository.CookieAuthorizationRequestRepository;
import com.eastshine.looknshop.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .headers().frameOptions().disable().and() // h2-console을 위한 설정. 나중에 사용하지 않을 때 삭제
                .httpBasic().disable() // ui를 사용하는 기본값 시큐리티 설정을 비활성화
                .csrf().disable() // csrf 보안 비활성화
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // RestAPI 기반 애플리케이션 동작방식. JWT 토큰 인증으로 세션 사용 않기 때문에 STATELESS

                // 요청에 대한 사용 권한 체크
                .and()
                .authorizeRequests()
                .antMatchers("/oauth2/**", "/api/users/login", "/api/users/signup").permitAll()
                .antMatchers("**exception**").permitAll() // 해당 패턴에 대해 모두 허용
                .antMatchers("/api/orders").authenticated()
                .anyRequest().authenticated()

                .and()
                .oauth2Login()
                .authorizationEndpoint().baseUri("/oauth2/authorize")  // 소셜 로그인 url
                .authorizationRequestRepository(cookieAuthorizationRequestRepository)  // 인증 요청을 cookie 에 저장
                .and()
                .redirectionEndpoint().baseUri("/oauth2/callback/*")  // 소셜 인증 후 redirect url
                .and()
                //userService()는 OAuth2 인증 과정에서 Authentication 생성에 필요한 OAuth2User 를 반환하는 클래스를 지정한다.
                .userInfoEndpoint().userService(customOAuth2UserService)  // 회원 정보 처리
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)

                .and()
                .logout()
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")

                //jwt filter 설정
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class); // Username~ 클래스 앞에 JwtTokenFilter를 추가

//                .and()
//                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()) // 권한 예외 처리
//
//                .and()
//                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 예외 처리



        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2"); // 인증, 인가 무시 경로 설정
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


}
