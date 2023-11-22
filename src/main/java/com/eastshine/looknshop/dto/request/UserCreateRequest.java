package com.eastshine.looknshop.dto.request;

import com.eastshine.looknshop.domain.User;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserCreateRequest {
    private String loginId;
    private String password;

    public User toEntity() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .build();
    }

}
