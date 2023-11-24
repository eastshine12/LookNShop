package com.eastshine.looknshop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users u SET u.is_deleted = true, u.deleted_at = now() WHERE u.id = ?")
@Table(name = "users")
@Entity
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    private String name;

    private String nickname;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
//    @Column(nullable = false) // dafault 값 지정하기
    private Grade grade = Grade.NEWBIE;

    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    @ColumnDefault("USER") // dafault 값 지정하기
    private Role role = Role.USER;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDateTime deletedAt;


    @Builder
    public User (String loginId, String password, String name, String nickname, String email, String phone, Grade grade, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.grade = grade;
        this.role = role;
    }


    public enum Grade {
        NEWBIE, BRONZE, SILVER, GOLD, PLATINUM, DIAMOND
    }

    public enum Role {
        USER, PARTNER, ADMIN
    }


}

