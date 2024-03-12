package com.eastshine.looknshop.domain;

import com.eastshine.looknshop.domain.Product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Wishlist extends BaseEntity {

    @EmbeddedId
    private WishlistId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDateTime deletedAt;

    @Embeddable
    public static class WishlistId implements Serializable {

        @Column(name = "user_id")
        private Long userId;

        @Column(name = "product_id")
        private Long productId;

    }


}
