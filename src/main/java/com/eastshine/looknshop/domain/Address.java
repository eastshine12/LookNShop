package com.eastshine.looknshop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String mainAddress;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private String phone;

    private String deliveryRequest;

    @Column(nullable = false)
    private boolean isDefault;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDateTime deletedAt;



}


