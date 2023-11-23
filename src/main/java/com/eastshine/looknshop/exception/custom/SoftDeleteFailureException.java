package com.eastshine.looknshop.exception.custom;

public class SoftDeleteFailureException extends RuntimeException {

    public SoftDeleteFailureException() {
        super();
    }

    public SoftDeleteFailureException(String message) {
        super(message);
    }
}
