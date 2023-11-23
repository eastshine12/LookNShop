package com.eastshine.looknshop.service;

import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.exception.custom.DuplicateLoginIdException;
import com.eastshine.looknshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long join(UserCreateRequest request) {
        boolean isDuplicatedLoginId = isDuplicatedLoginId(request.getLoginId());
        if(isDuplicatedLoginId) {
            throw new DuplicateLoginIdException("Duplicate Login ID: " + request.getLoginId());
        }
        return userRepository.save(request.toEntity()).getId();
    }

    public boolean isDuplicatedLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public boolean isValidUser() {
        return false;
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public User findUserByEmail(String email) {
        return null;
    }
}
