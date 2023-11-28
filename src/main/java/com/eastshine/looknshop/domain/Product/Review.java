package com.eastshine.looknshop.domain.Product;

import com.eastshine.looknshop.domain.BaseEntity;
import com.eastshine.looknshop.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int rating;

    private String title;

    private String content;

    private int helpfulCount;

    private String image;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDateTime deletedAt;


}
