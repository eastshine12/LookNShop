package com.eastshine.looknshop.exception.custom;

public class ProductCategoryNotFoundException extends RuntimeException {

    public ProductCategoryNotFoundException() {
        super();
    }
    public ProductCategoryNotFoundException(String message) {
        super(message);
    }
}
