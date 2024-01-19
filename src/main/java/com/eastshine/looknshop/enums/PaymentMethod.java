package com.eastshine.looknshop.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CARD,         // 카드
    VIRTUAL_ACC,  // 가상계좌
    EASY_PAY,     // 간편결제
    MOBILE,       // 휴대폰
    BANK_TRANSFER // 계좌이체
}
