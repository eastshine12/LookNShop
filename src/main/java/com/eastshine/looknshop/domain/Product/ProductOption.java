package com.eastshine.looknshop.domain.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;

    private String value;

    private int price;

    private int stockQuantity;

    @Builder
    public ProductOption(Product product, String name, String value, int price, int stockQuantity) {
        this.product = product;
        this.name = name;
        this.value = value;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
