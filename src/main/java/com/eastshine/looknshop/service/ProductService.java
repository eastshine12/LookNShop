package com.eastshine.looknshop.service;

import com.eastshine.looknshop.domain.Product.Product;
import com.eastshine.looknshop.domain.Product.ProductCategory;
import com.eastshine.looknshop.domain.Product.ProductOption;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.ProductCreateRequest;
import com.eastshine.looknshop.dto.request.ProductOptionDto;
import com.eastshine.looknshop.dto.response.ProductResponse;
import com.eastshine.looknshop.exception.custom.ProductCategoryNotFoundException;
import com.eastshine.looknshop.exception.custom.ProductNotFoundException;
import com.eastshine.looknshop.exception.custom.StockQuantityMismatchException;
import com.eastshine.looknshop.repository.ProductCategoryRepository;
import com.eastshine.looknshop.repository.ProductOptionRepository;
import com.eastshine.looknshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public Long createProduct(User user, ProductCreateRequest request) {

        validateStockQuantity(request.getProductOptions(), request.getTotalStock());
        String thumbnail1FileName = fileStorageService.storeFile(request.getThumbnail1());
        String thumbnail2FileName = request.getThumbnail2() != null ? fileStorageService.storeFile(request.getThumbnail2()) : null;
        ProductCategory productCategory = productCategoryRepository.findById(request.getCategoryId()).orElseThrow(ProductCategoryNotFoundException::new);

        Product product = Product.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .category(productCategory)
                .price(request.getPrice())
                .discountRate(request.getDiscountRate())
                .totalStock(request.getTotalStock())
                .thumbnail1(thumbnail1FileName)
                .thumbnail2(thumbnail2FileName)
                .build();

        productRepository.save(product);

        if(request.getProductOptions() != null && !request.getProductOptions().isEmpty()) {
            saveProductOptions(request, product);
        }

        return product.getId();
    }

    public void validateStockQuantity(List<ProductOptionDto> productOptionDtos, int totalStock) {
        int sum = 0;
        for (ProductOptionDto dto : productOptionDtos) {
            sum += dto.getStockQuantity();
        }

        if (sum != totalStock) {
            throw new StockQuantityMismatchException("총 재고 수량과 합산된 수량이 일치하지 않습니다.");
        }
    }

    private void saveProductOptions(ProductCreateRequest request, Product product) {
        for (ProductOptionDto dto : request.getProductOptions()) {
            ProductOption productOption = ProductOption.builder()
                    .product(product)
                    .name(dto.getName())
                    .value(dto.getValue())
                    .price(dto.getPrice())
                    .stockQuantity(dto.getStockQuantity())
                    .build();

            productOptionRepository.save(productOption);
        }
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
                .productOptionList(product.getProductOptions())
                .build();
    }

    private String getThumbnailPath(String thumbnail) {
        return thumbnail == null ? null : "/images/" + thumbnail;
    }


    public Product getProductById(Long productId) {
        return productRepository.findByIdWithCategoryAndOption(productId).orElseThrow(ProductNotFoundException::new);
    }
}
