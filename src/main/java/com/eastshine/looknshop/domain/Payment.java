package com.eastshine.looknshop.domain;

import com.eastshine.looknshop.enums.PaymentMethod;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private int amount;

    private LocalDateTime paymentTime;


}
