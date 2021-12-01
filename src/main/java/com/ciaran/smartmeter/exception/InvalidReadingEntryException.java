package com.ciaran.smartmeter.exception;

public class InvalidReadingEntryException extends RuntimeException{
    private String errorMessage;

    public InvalidReadingEntryException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
