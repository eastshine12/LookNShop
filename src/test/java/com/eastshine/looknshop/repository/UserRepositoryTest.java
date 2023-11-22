package com.eastshine.looknshop.repository;

import com.eastshine.looknshop.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void save() {
        // given
        User user = User.builder()
                .loginId("test01")
                .password("1q2w3e4r!")
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        Assertions.assertThat(savedUser.getLoginId()).isEqualTo(user.getLoginId());
    }

}