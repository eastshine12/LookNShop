package com.eastshine.looknshop.controller;

import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> join(@RequestBody @Valid UserCreateRequest request) {
        log.info("UserController join()");
        Long id = userService.join(request);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. id = " + id);
    }

}
