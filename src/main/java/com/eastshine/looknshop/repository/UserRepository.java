package com.eastshine.looknshop.repository;

import com.eastshine.looknshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginId(String loginId);
    Optional<User> findByLoginId(String loginId);
    boolean existsByIdAndIsDeletedTrue(Long id);
}

