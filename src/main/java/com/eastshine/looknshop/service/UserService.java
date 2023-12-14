package com.eastshine.looknshop.service;

import com.eastshine.looknshop.config.jwt.JwtTokenProvider;
import com.eastshine.looknshop.config.jwt.RefreshTokenService;
import com.eastshine.looknshop.config.oauth2.UserPrincipal;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.dto.request.UserLoginRequest;
import com.eastshine.looknshop.dto.response.UserLoginResponse;
import com.eastshine.looknshop.exception.custom.DuplicateLoginIdException;
import com.eastshine.looknshop.exception.custom.PasswordNotMatchedException;
import com.eastshine.looknshop.exception.custom.SoftDeleteFailureException;
import com.eastshine.looknshop.exception.custom.UserNotFoundException;
import com.eastshine.looknshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenService refreshTokenService;

    public Long join(UserCreateRequest request) {
        isDuplicatedLoginId(request.getLoginId());
        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        return userRepository.save(request.toEntity()).getId();
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest request, HttpServletResponse response) {
        User findUser = findUserByLoginId(request.getLoginId());
        validUser(request, findUser);

        UserLoginResponse.TokenInfo tokenInfo = getTokenInfo(findUser);

        refreshTokenService.updateOrSaveRefreshToken(findUser, tokenInfo);

        jwtTokenProvider.sendAccessAndRefreshToken(response, tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());

        return UserLoginResponse.builder()
                .userId(findUser.getId())
                .loginId(findUser.getLoginId())
                .name(findUser.getName())
                .phone(findUser.getPhone())
                .nickname(findUser.getNickname())
                .grade(findUser.getGrade())
                .build();
    }

    private UserLoginResponse.TokenInfo getTokenInfo(User findUser) {
        return jwtTokenProvider.generateToken(findUser.getEmail(), UserPrincipal.create(findUser).getAuthorities());
    }

    ;

    public void isDuplicatedLoginId(String loginId) {
        boolean isDuplicatedLoginId = userRepository.existsByLoginId(loginId);
        if(isDuplicatedLoginId) {
            throw new DuplicateLoginIdException("Duplicate Login ID: " + loginId);
        }

    }

    public void validUser(UserLoginRequest request, User user) {
        if(!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchedException("Not matches Password");
        }
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User deleteUser = findUserById(userId);
        userRepository.delete(deleteUser);
        boolean isDeleted = userRepository.existsByIdAndIsDeletedTrue(userId);
        if (!isDeleted) {
            throw new SoftDeleteFailureException();
        }
    }

}
