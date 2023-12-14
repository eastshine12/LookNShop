package com.eastshine.looknshop.controller;

import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.dto.request.UserLoginRequest;
import com.eastshine.looknshop.dto.response.UserLoginResponse;
import com.eastshine.looknshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Tag(name = "User", description = "User API")
@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입 API", description = "가입 정보를 받아 중복 ID 조회 후 회원을 등록한다.")
    @PostMapping("/signup")
    public ResponseEntity<String> join(@Valid UserCreateRequest request) {
        log.info("UserController join()");
        Long id = userService.join(request);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. id = " + id);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request, HttpServletResponse response) {
        log.info("UserController login()");
        UserLoginResponse userLoginResponse = userService.login(request, response);

        return ResponseEntity.status(HttpStatus.OK).body(userLoginResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("UserController logout()");
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.status(HttpStatus.OK).body("logout");
    }

    @Operation(summary = "회원 조회 API", description = "user_id로 회원 정보를 조회한다.")
    @GetMapping
    public ResponseEntity<User> getUser(Long userId) {
        log.info("UserController getUser()");
        User user = userService.findUserById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "회원 삭제 API", description = "user_id로 회원 정보를 삭제한다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable Long userId) {
        log.info("UserController delete()");
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully.");
    }

}
