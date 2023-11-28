package com.eastshine.looknshop.dto.request;

import com.eastshine.looknshop.domain.User;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class UserLoginRequest {

    @NotNull
    @Size(min = 5, max = 20)
    private String loginId;
    @NotNull
    private String password;

    public User toEntity() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .build();
    }

}
