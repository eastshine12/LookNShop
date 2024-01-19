package com.eastshine.looknshop.domain.Product;

import javax.persistence.*;

@Entity
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;

    private String value;

    private int price;

    private int stockQuantity;
}
