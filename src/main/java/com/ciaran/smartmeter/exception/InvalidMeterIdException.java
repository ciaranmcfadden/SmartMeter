package com.ciaran.smartmeter.exception;

public class InvalidMeterIdException extends RuntimeException{
    private String errorMessage;

    public InvalidMeterIdException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
