package com.eastshine.looknshop.dto.response;

import com.eastshine.looknshop.domain.Product.ProductOption;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class ProductResponse {

    private Long productId, partnerId;
    private String title, content, thumbnail1, thumbnail2, partnerName, categoryName;
    private int price, discountRate, totalStock;
    private List<ProductOption> productOptionList;

}
