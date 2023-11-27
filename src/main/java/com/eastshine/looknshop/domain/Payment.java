package com.eastshine.looknshop.domain;

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

    public enum PaymentMethod {
        CARD,         // 카드
        VIRTUAL_ACC,  // 가상계좌
        EASY_PAY,     // 간편결제
        MOBILE,       // 휴대폰
        BANK_TRANSFER // 계좌이체
    }

}
