package com.eastshine.looknshop.exception.custom;

public class DuplicateLoginIdException extends RuntimeException {

    public DuplicateLoginIdException(String message) {
        super(message);
    }

}
