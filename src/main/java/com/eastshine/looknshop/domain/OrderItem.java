package com.eastshine.looknshop.domain;

import com.eastshine.looknshop.domain.Product.Product;
import com.eastshine.looknshop.domain.Product.ProductOption;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Product_option_id")
    private ProductOption ProductOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;

    private int orderPrice;


}
