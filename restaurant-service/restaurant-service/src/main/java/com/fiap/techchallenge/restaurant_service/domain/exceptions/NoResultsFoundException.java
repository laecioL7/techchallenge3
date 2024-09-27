package com.fiap.techchallenge.restaurant_service.domain.exceptions;

public class NoResultsFoundException extends RuntimeException {
    public NoResultsFoundException(String message) {
        super(message);
    }
}