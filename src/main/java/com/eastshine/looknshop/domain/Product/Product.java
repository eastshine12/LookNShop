package com.eastshine.looknshop.domain.Product;

import com.eastshine.looknshop.domain.BaseEntity;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.exception.custom.OutOfStockException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE product p SET p.is_deleted = true, p.deleted_at = now() WHERE p.product_id = ?")
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User partner;

    @OneToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<ProductOption> productOptions = new ArrayList<>();

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
    public Product(User user, ProductCategory category, List<ProductOption> productOptions, String title, String content, String thumbnail1, String thumbnail2, int price, int discountRate, int totalStock) {
        this.partner = user;
        this.category = category;
        this.productOptions = productOptions;
        this.title = title;
        this.content = content;
        this.thumbnail1 = thumbnail1;
        this.thumbnail2 = thumbnail2;
        this.price = price;
        this.discountRate = discountRate;
        this.totalStock = totalStock;
    }

    public void removeStock(int quantity) {
        int restStock = this.totalStock - quantity;
        if (restStock < 0) {
            throw new OutOfStockException("Not enough stock available for product: " + this.title);
        }
        this.totalStock = restStock;
    }

}
