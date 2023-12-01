package com.eastshine.looknshop.service;

import com.eastshine.looknshop.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

//    public String createNewAccessToken(String refreshToken) {
//        // 토큰 유효성 검사에 실패하면 예외 발생
//        if (!jwtTokenProvider.validateToken(refreshToken)) {
//            throw new IllegalArgumentException("Unexpected token");
//        }
//
//        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUser().getId();
//        User user = userService.findUserById(userId);
//
//        return jwtTokenProvider.generateToken(String.valueOf(userId), user.getRole());
//    }
}
