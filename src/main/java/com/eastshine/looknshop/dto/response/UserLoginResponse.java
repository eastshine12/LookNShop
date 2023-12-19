package com.eastshine.looknshop.dto.response;

import com.eastshine.looknshop.enums.Grade;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class UserLoginResponse {

    private Long userId;
    private String loginId, name, nickname, phone, email;
    private Grade grade;
    private TokenInfo tokenInfo;

    @Data
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private Long accessTokenExpirationTime;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }

}
