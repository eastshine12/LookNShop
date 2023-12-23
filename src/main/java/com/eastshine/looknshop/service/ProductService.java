package com.eastshine.looknshop.service;

import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.ProductCreateRequest;
import com.eastshine.looknshop.exception.custom.UserNotFoundException;
import com.eastshine.looknshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final UserRepository userRepository;

    public Long createPost(User user, ProductCreateRequest request) {
        log.info("유저 email : {}", user.getEmail());
        return null;
    }
}
