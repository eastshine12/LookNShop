package com.eastshine.looknshop.config.jwt;

import com.eastshine.looknshop.config.oauth2.UserPrincipal;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.response.UserLoginResponse;
import com.eastshine.looknshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public UserLoginResponse.TokenInfo createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUser().getId();
        User user = userService.findUserById(userId);
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        return jwtTokenProvider.generateToken(userPrincipal.getName(), userPrincipal.getAuthorities());
    }
}
