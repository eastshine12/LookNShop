package com.eastshine.looknshop.service;

import com.eastshine.looknshop.domain.Product.Product;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.ProductCreateRequest;
import com.eastshine.looknshop.dto.response.ProductResponse;
import com.eastshine.looknshop.exception.custom.ProductNotFoundException;
import com.eastshine.looknshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    public Long createProduct(User user, ProductCreateRequest request) {
        String thumbnail1FileName = fileStorageService.storeFile(request.getThumbnail1());
        String thumbnail2FileName = null;
        if (request.getThumbnail2() != null) {
            thumbnail2FileName = fileStorageService.storeFile(request.getThumbnail2());
        }

        return productRepository.save(Product.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .price(request.getPrice())
                .discountRate(request.getDiscountRate())
                .totalStock(request.getTotalStock())
                .thumbnail1(thumbnail1FileName)
                .thumbnail2(thumbnail2FileName)
                .build()).getId();
    }

    public ProductResponse getProduct(Long productId) {
        Product product = getProductById(productId);
        return convertToProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse convertToProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getId())
                .partnerId(product.getPartner().getId())
                .partnerName(product.getPartner().getName())
                .title(product.getTitle())
                .content(product.getContent())
                .price(product.getPrice())
                .discountRate(product.getDiscountRate())
                .totalStock(product.getTotalStock())
                .thumbnail1(getThumbnailPath(product.getThumbnail1()))
                .thumbnail2(getThumbnailPath(product.getThumbnail2()))
                .categoryName(product.getCategory().getName())
                .build();
    }

    private String getThumbnailPath(String thumbnail) {
        return thumbnail == null ? null : "/images/" + thumbnail;
    }


    public Product getProductById(Long productId) {
        return productRepository.findByIdWithCategory(productId).orElseThrow(ProductNotFoundException::new);
    }
}
