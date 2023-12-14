package com.eastshine.looknshop.config.jwt;

import com.eastshine.looknshop.dto.response.UserLoginResponse;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

//    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
//    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";



    @PostConstruct
    protected void init() {
        log.info("[init] JwtTokenProvider secretKey 초기화");

        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //Authentication 을 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public UserLoginResponse.TokenInfo generateToken(Authentication authentication) {
        return generateToken(authentication.getName(), authentication.getAuthorities());
    }

    public UserLoginResponse.TokenInfo generateToken(String name, Collection<? extends GrantedAuthority> inputAuthorities) {
        log.info("JwtTokenProvider generateToken()");
        //권한 가져오기
        String authorities = inputAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        //Generate AccessToken
        String accessToken = Jwts.builder()
                .setSubject(name)
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(now)   //토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + accessTokenExpirationPeriod))  //토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        //Generate RefreshToken
        String refreshToken = Jwts.builder()
                .claim("type", TYPE_REFRESH)
                .setIssuedAt(now)   //토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationPeriod)) //토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();


        UserLoginResponse.TokenInfo tokenInfo = UserLoginResponse.TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(accessTokenExpirationPeriod)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(refreshTokenExpirationPeriod)
                .build();

        refreshTokenService.updateOrSaveRefreshToken(name, tokenInfo);


        return tokenInfo;
    }


    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    public Authentication getAuthentication(String token) {
        log.info("[getAuthentication] JwtTokenProvider token 인증 정보 조회");

        //토큰 복호화
        Claims claims = parseClaims(token);

        if (claims.get(AUTHORITIES_KEY) == null) {
            //TODO:: Change Custom Exception
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        //클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

        /*
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        */
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getUserId(String token) {
        log.info("[getAuthentication] JwtTokenProvider getUsername");

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserRole(String token) {
        log.info("[getAuthentication] JwtTokenProvider getUserRole");

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("auth", String.class);
    }

    public String resolveToken(HttpServletRequest req) {
        log.info("[resolveToken] JwtTokenProvider resolveToken");

        String bearerToken = req.getHeader(accessHeader);
        if (bearerToken != null && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        log.info("[validateToken] JwtTokenProvider token 유효성 체크");
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("[validateToken] JwtTokenProvider Expired or invalid JWT token.");
            return false;
        }
    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

}