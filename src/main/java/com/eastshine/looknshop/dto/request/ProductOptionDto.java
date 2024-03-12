package com.eastshine.looknshop.dto.request;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class ProductOptionDto {

    private String name, value;
    private Integer id, price, stockQuantity;

}
