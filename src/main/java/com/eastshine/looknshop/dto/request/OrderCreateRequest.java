package com.eastshine.looknshop.dto.request;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class OrderCreateRequest {

    private Long productId, productOptionId;
    private Integer quantity;

}
