package com.eastshine.looknshop.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class ProductResponse {

    private Long product_id, partner_id;
    private String title, content, thumbnail1, thumbnail2;
    private int price, discountRate, totalStock;

}
