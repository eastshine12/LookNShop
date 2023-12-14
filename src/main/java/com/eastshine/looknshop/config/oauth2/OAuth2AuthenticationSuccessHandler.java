package com.eastshine.looknshop.config.oauth2;

import com.eastshine.looknshop.config.jwt.JwtTokenProvider;
import com.eastshine.looknshop.dto.response.UserLoginResponse;
import com.eastshine.looknshop.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.eastshine.looknshop.config.oauth2.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth.authorizedRedirectUri}")
    private String authorizedRedirectUri;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        log.info("OAuth2AuthenticationSuccessHandler onAuthenticationSuccess() targetUrl = {}", targetUrl);
        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RuntimeException("redirect URIs are not matched.");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        log.info("OAuth2AuthenticationSuccessHandler determineTargetUrl() targetUrl : {}", targetUrl);
        //JWT 생성
        UserLoginResponse.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        log.info("OAuth2AuthenticationSuccessHandler determineTargetUrl() tokenInfo : {}", tokenInfo.toString());
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access-token", tokenInfo.getAccessToken())
                .queryParam("refresh-token", tokenInfo.getRefreshToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(authorizedRedirectUri);
        log.info("isAuthorizedRedirectUri() client: {}, server: {}", clientRedirectUri.toString(), authorizedUri.toString());
        return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort();
    }
}