package com.eastshine.looknshop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String orderCancelReason;

    public enum OrderStatus {
        ORDERED, //주문완료
        CANCELLED //주문취소
    }

    @Builder
    public Order(User user, List<OrderItem> orderItems, LocalDateTime orderDate, Payment payment, OrderStatus status, String orderCancelReason) {
        this.user = user;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
        this.payment = payment;
        this.status = status;
        this.orderCancelReason = orderCancelReason;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getOrderPrice();
        }
        return totalPrice;
    }

}
