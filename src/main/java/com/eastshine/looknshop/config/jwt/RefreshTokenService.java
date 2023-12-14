package com.eastshine.looknshop.config.jwt;

import com.eastshine.looknshop.domain.RefreshToken;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.response.UserLoginResponse;
import com.eastshine.looknshop.exception.custom.UserNotFoundException;
import com.eastshine.looknshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

    public void updateOrSaveRefreshToken(User findUser, UserLoginResponse.TokenInfo tokenInfo) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser(findUser);

        if (optionalRefreshToken.isPresent()) {
            RefreshToken existingRefreshToken = optionalRefreshToken.get();
            existingRefreshToken.updateToken(tokenInfo.getRefreshToken());
            refreshTokenRepository.save(existingRefreshToken);
        } else {
            refreshTokenRepository.save(RefreshToken.builder()
                    .user(findUser)
                    .token(tokenInfo.getRefreshToken())
                    .build());
        }
    }

    public void updateOrSaveRefreshToken(String email, UserLoginResponse.TokenInfo tokenInfo) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        updateOrSaveRefreshToken(user, tokenInfo);
    }

}
