package com.robot.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WrongDatesException extends RuntimeException {
    public WrongDatesException(String message) {
        super(message);
    }
}