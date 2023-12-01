package com.eastshine.looknshop.service;

import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.dto.request.UserLoginRequest;
import com.eastshine.looknshop.exception.custom.DuplicateLoginIdException;
import com.eastshine.looknshop.exception.custom.PasswordNotMatchedException;
import com.eastshine.looknshop.exception.custom.SoftDeleteFailureException;
import com.eastshine.looknshop.exception.custom.UserNotFoundException;
import com.eastshine.looknshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long join(UserCreateRequest request) {
        boolean isDuplicatedLoginId = isDuplicatedLoginId(request.getLoginId());
        if(isDuplicatedLoginId) {
            throw new DuplicateLoginIdException("Duplicate Login ID: " + request.getLoginId());
        }
        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        return userRepository.save(request.toEntity()).getId();
    }

    public boolean isDuplicatedLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public void validUser(UserLoginRequest request) {
        User findUser = findUserByLoginId(request.getLoginId());
        if(bCryptPasswordEncoder.matches(request.getPassword(), findUser.getPassword())) {
            throw new PasswordNotMatchedException("Not matches Password");
        }
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new);
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

    public User findUserByEmail(String email) {
        return null;
    }
}
