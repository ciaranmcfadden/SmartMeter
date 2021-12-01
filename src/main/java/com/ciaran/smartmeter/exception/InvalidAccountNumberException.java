package com.ciaran.smartmeter.exception;

public class InvalidAccountNumberException extends RuntimeException{
    private String errorMessage;

    public InvalidAccountNumberException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
