package com.eastshine.looknshop.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class ProductCreateRequest {

    @NotNull
    private String title, content;
    @NotNull
    private Integer price, discountRate, totalStock;
    @NotNull
    private Long categoryId;
    @NotNull
    private MultipartFile thumbnail1;
    private MultipartFile thumbnail2;
    private List<ProductOptionDto> productOptions;

}
