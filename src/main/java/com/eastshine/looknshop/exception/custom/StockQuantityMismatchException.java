package com.eastshine.looknshop.exception.custom;

public class StockQuantityMismatchException extends RuntimeException {

    public StockQuantityMismatchException() {
        super();
    }
    public StockQuantityMismatchException(String message) {
        super(message);
    }
}
