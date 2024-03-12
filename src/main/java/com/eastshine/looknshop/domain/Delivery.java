package com.eastshine.looknshop.domain;

import com.eastshine.looknshop.enums.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private LocalDateTime deliveryStartTime; // 배송 시작 시간

    private LocalDateTime deliveryCompletedTime; // 배송 완료 시간

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

}

