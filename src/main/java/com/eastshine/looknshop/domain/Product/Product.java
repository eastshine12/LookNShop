package com.eastshine.looknshop.domain.Product;

import com.eastshine.looknshop.domain.BaseEntity;
import com.eastshine.looknshop.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Product(User user, String title, String content, String thumbnail1, String thumbnail2, int price, int discountRate, int totalStock) {
        this.partner = user;
        this.title = title;
        this.content = content;
        this.thumbnail1 = thumbnail1;
        this.thumbnail2 = thumbnail2;
        this.price = price;
        this.discountRate = discountRate;
        this.totalStock = totalStock;
    }

}
