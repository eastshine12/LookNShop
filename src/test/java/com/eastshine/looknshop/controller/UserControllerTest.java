package com.eastshine.looknshop.controller;

import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.exception.GlobalExceptionHandler;
import com.eastshine.looknshop.exception.custom.DuplicateLoginIdException;
import com.eastshine.looknshop.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    /* Mock 으로 선언된 객체(userService)를 의존하는 Controller 객체(userController) 생성 */
    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() { // mockMvc 초기화
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler()) // GlobalExceptionHandler 추가
                .build();
    }

    @Test
    @DisplayName("회원 가입에 성공한다. ")
    public void join() throws Exception {

        // given
        final String url = "/api/users";

        UserCreateRequest request = UserCreateRequest.builder()
                .loginId("test01")
                .password("1q2w3e4r!")
                .build();

        long userId = 1L;
        when(userService.join(any(UserCreateRequest.class))).thenReturn(userId);

        // when
        ResultActions resultActions = mockMvc.perform(
                        post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully. id = " + userId));

        verify(userService, times(1)).join(any(UserCreateRequest.class));
    }


    @Test
    @DisplayName("회원 가입 시 중복 ID가 존재하여 가입에 실패한다. ")
    public void duplicateLoginId() throws Exception {

        // given
        final String url = "/api/users";

        UserCreateRequest request = UserCreateRequest.builder()
                .loginId("test01")
                .password("1q2w3e4r!")
                .build();

        when(userService.join(any(UserCreateRequest.class)))
                .thenThrow(new DuplicateLoginIdException("Duplicate Login ID"));

        // when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Duplicate Login ID"));
    }
}