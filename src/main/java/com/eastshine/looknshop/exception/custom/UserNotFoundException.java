package com.eastshine.looknshop.exception.custom;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super();
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
