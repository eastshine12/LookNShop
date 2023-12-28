package com.eastshine.looknshop.exception.custom;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super();
    }
    public ProductNotFoundException(String message) {
        super(message);
    }
}
