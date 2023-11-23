package com.eastshine.looknshop.service;

import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.exception.custom.DuplicateLoginIdException;
import com.eastshine.looknshop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;


    /* Mock 으로 선언된 객체(userRepository)를 의존하는 Service 객체(userService) 생성 */
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("아이디, 패스워드가 입력될 시 가입에 성공한다.")
    void join() {

        // given

        UserCreateRequest req = UserCreateRequest.builder()
                .loginId("test1")
                .password("1q2w3e4r!")
                .build();

        User mockUser = User.builder()
                .loginId("test1")
                .password("1q2w3e4r!")
                .build();

        /* 유닛 테스트를 위해 Reflection 사용 */
        Long fakeUserId = 1L;
        ReflectionTestUtils.setField(mockUser, "id", fakeUserId);

        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userRepository.findById(fakeUserId)).thenReturn(Optional.of(mockUser));

        // when
        Long id = userService.join(req);

        // then
        User findUser = userRepository.findById(id).get();
        assertThat(findUser.getLoginId()).isEqualTo(mockUser.getLoginId());
    }


    @Test
    @DisplayName("회원 가입 시 중복 ID가 존재하여 가입에 실패한다. ")
    public void duplicateLoginId() throws Exception {

        // given
        UserCreateRequest req = UserCreateRequest.builder()
                .loginId("test01")
                .password("1q2w3e4r!")
                .build();

        when(userRepository.existsByLoginId(anyString())).thenReturn(true);

        // when & then
        assertThatExceptionOfType(DuplicateLoginIdException.class)
                .isThrownBy(() -> userService.join(req));
    }

}