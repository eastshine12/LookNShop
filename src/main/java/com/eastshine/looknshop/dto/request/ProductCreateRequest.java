package com.eastshine.looknshop.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class ProductCreateRequest {

    private String title, content;
    private int price,discountRate, totalStock;
    private MultipartFile thumbnail1, thumbnail2;

}
