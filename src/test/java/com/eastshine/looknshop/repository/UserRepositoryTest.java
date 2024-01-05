package com.eastshine.looknshop.repository;

import com.eastshine.looknshop.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;
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
        assertThat(savedUser.getLoginId()).isEqualTo(user.getLoginId());
    }


    @Test
    public void delete() {

        // given
        User user = User.builder()
                .loginId("test01")
                .password("1q2w3e4r!")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();

        // when
        userRepository.deleteById(savedUser.getId());
        testEntityManager.flush();

        // then
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertThat(deletedUser.get().getIsDeleted()).isTrue();
        assertThat(deletedUser.get().getDeletedAt()).isNotNull();
    }

}