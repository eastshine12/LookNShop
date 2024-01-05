package com.eastshine.looknshop.domain;

import com.eastshine.looknshop.config.oauth2.OAuth2UserInfo;
import com.eastshine.looknshop.enums.AuthProvider;
import com.eastshine.looknshop.enums.Grade;
import com.eastshine.looknshop.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users u SET u.is_deleted = true, u.deleted_at = now() WHERE u.id = ?")
@Table(name = "users")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    private String name;

    private String nickname;

    @Column(nullable = false)
    private String email;

    private String phone;

    private String oauth2Id;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        if (grade == null) {
            grade = Grade.NEWBIE;
        }
        if (role == null) {
            role = Role.USER;
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    @Builder
    public User (String loginId, String password, String name, String nickname, String email, String phone, String oauth2Id, AuthProvider authProvider, Grade grade, Role role, Boolean isDeleted, LocalDateTime deletedAt) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.oauth2Id = oauth2Id;
        this.authProvider = authProvider;
        this.grade = grade;
        this.role = role;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }


    public User update(OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.oauth2Id = oAuth2UserInfo.getOAuth2Id();

        return this;
    }


}

