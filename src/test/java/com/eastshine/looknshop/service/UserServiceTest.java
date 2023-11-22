package com.eastshine.looknshop.service;

import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.UserCreateRequest;
import com.eastshine.looknshop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


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

        UserCreateRequest req = new UserCreateRequest("test1", "1q2w3e4r!");

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

}