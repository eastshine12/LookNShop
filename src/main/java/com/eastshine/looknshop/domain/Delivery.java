package com.eastshine.looknshop.domain;

import javax.persistence.*;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public enum deliveryStatus {
        PREPARING, //배송준비
        SHIPPING, //배송중
        DELIVERED //배송완료
    }
}

