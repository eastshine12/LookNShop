package com.eastshine.looknshop.domain.Product;

import com.eastshine.looknshop.domain.BaseEntity;
import com.eastshine.looknshop.domain.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User partner;

    private String title;

    private String content;

    private String thumbnail1;

    private String thumbnail2;

    private int price;

    private int discountRate;

    private int totalStock;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDateTime deletedAt;
}
