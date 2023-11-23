package com.eastshine.looknshop.dto.request;

import com.eastshine.looknshop.domain.User;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class UserCreateRequest {

    @NotNull
    @Size(min = 5, max = 20)
    private String loginId;
    @NotNull
    private String password;
    private String name;
    private String nickname;
    private String email;
    private String phone;

    public User toEntity() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .build();
    }

}
