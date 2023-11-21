package com.eastshine.looknshop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

//    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // ui를 사용하는 기본값 시큐리티 설정을 비활성화
                .csrf().disable() // csrf 보안 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // RestAPI 기반 애플리케이션 동작방식. JWT 토큰 인증으로 세션 사용 않기 때문에 STATELESS
                .and()
                .authorizeRequests() // 요청에 대한 사용 권한 체크
                .antMatchers("/api/orders").authenticated()
                .antMatchers("**").permitAll()
                .antMatchers("**exception**").permitAll(); // 해당 패턴에 대해 모두 허용
//                .anyRequest().hasRole("ADMIN")
//
//                .and()
//                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()) // 권한 예외 처리
//
//                .and()
//                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 예외 처리
//
//                .and()
//                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider),
//                        UsernamePasswordAuthenticationFilter.class); // Username~ 클래스 앞에 JwtTokenFilter를 추가

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
