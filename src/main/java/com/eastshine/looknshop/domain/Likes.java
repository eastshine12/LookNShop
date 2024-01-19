package com.eastshine.looknshop.domain;

import com.eastshine.looknshop.enums.PostType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    private Long targetId;

    private LocalDateTime likedAt;


}

