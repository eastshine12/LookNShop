package com.eastshine.looknshop.enums;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    PREPARING, // 배송 준비
    SHIPPING, // 배송 중
    DELIVERED, // 배송 완료
    CANCELED    // 배송 취소
}
