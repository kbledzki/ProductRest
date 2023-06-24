package com.java.ProductRest.exception;

public class ProductServiceBusinessException extends RuntimeException{
    public ProductServiceBusinessException(String message) {
        super(message);
    }
}
