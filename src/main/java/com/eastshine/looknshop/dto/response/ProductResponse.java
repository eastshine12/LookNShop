package com.eastshine.looknshop.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class ProductResponse {

    private Long productId, partnerId;
    private String title, content, thumbnail1, thumbnail2, partnerName, categoryName;
    private int price, discountRate, totalStock;

}
