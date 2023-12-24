package com.eastshine.looknshop.service;

import com.eastshine.looknshop.domain.Product.Product;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.ProductCreateRequest;
import com.eastshine.looknshop.repository.ProductRepository;
import com.eastshine.looknshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    public Long createPost(User user, ProductCreateRequest request) {
        String thumbnail1FileName = fileStorageService.storeFile(request.getThumbnail1());
        String thumbnail2FileName = null;
        if (request.getThumbnail2() != null) {
            thumbnail2FileName = fileStorageService.storeFile(request.getThumbnail2());
        }

        return productRepository.save(Product.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .price(request.getPrice())
                .discountRate(request.getDiscountRate())
                .totalStock(request.getTotalStock())
                .thumbnail1(thumbnail1FileName)
                .thumbnail2(thumbnail2FileName)
                .build()).getId();
    }
}
