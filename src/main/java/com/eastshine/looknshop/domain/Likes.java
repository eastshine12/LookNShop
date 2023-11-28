package com.eastshine.looknshop.domain;

import javax.persistence.*;

@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private PostType postType;

    private Long entityId;



    public enum PostType {
        PRODUCT, REVIEW
    }

}

