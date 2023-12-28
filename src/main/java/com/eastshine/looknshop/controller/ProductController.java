package com.eastshine.looknshop.controller;

import com.eastshine.looknshop.annotation.CurrentUser;
import com.eastshine.looknshop.domain.User;
import com.eastshine.looknshop.dto.request.ProductCreateRequest;
import com.eastshine.looknshop.dto.response.ProductResponse;
import com.eastshine.looknshop.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Product", description = "Product API")
@RequestMapping("/api/products")
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@CurrentUser User user, @ModelAttribute ProductCreateRequest request) {
        log.info("ProductController createProduct()");
        Long productId = productService.createProduct(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product registered successfully. id = " + productId);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        log.info("ProductController getProduct()");
        ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

}
