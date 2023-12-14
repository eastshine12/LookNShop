package com.eastshine.looknshop.config.jwt;

import com.eastshine.looknshop.domain.RefreshToken;
import com.eastshine.looknshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
