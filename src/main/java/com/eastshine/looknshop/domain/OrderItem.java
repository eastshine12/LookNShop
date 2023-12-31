package com.eastshine.looknshop.domain;

import com.eastshine.looknshop.domain.Product.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;

    private int orderPrice;

    @Builder
    public OrderItem(Product product, Order order, int quantity, int orderPrice) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
    }

    public int getTotalPrice() {
        return orderPrice * quantity;
    }

}
