package com.eastshine.looknshop.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    USER, PARTNER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}