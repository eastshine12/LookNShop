package com.eastshine.looknshop.repository;

import com.eastshine.looknshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public boolean existsByLoginId(String loginId);
}
