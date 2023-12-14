package com.eastshine.looknshop.config.oauth2;

import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.enums.AuthProvider;
import com.eastshine.looknshop.enums.Role;
import com.eastshine.looknshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;



@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>  {

    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService loadUser()");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        return processOAuth2User(userRequest, oAuth2User);
    }

    protected OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        //OAuth2 로그인 플랫폼 구분(google, naver, kakao)
        AuthProvider authProviderId = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProviderId, oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        //이미 가입된 경우
        if (user != null) {
            if (!user.getAuthProvider().equals(authProviderId)) {
                throw new RuntimeException("Email already signed up.");
            }
            user = updateUser(user, oAuth2UserInfo);
        }
        //가입되지 않은 경우
        else {
            log.info("신규 가입");
            user = registerUser(authProviderId, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2UserInfo.getAttributes());
    }

    private User registerUser(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                .loginId(oAuth2UserInfo.getEmail()+"|"+oAuth2UserInfo.getOAuth2Id())
                .password("oauth")
                .email(oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getName())
                .oauth2Id(oAuth2UserInfo.getOAuth2Id())
                .authProvider(authProvider)
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    private User updateUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        return userRepository.save(user.update(oAuth2UserInfo));
    }

}
