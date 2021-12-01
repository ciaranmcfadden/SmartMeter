package com.ciaran.smartmeter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value = InvalidAccountNumberException.class)
    public ResponseEntity invalidAccountNumberException(InvalidAccountNumberException invalidAccountNumberException) {
        return new ResponseEntity(invalidAccountNumberException.getErrorMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidReadingEntryException.class)
    public ResponseEntity invalidReadingEntryException(InvalidReadingEntryException invalidReadingEntryException) {
        return new ResponseEntity(invalidReadingEntryException.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidMeterIdException.class)
    public ResponseEntity invalidMeterIdException(InvalidMeterIdException duplicateDateEntryException) {
        return new ResponseEntity(duplicateDateEntryException.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }

}
