package com.robot.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GuestsQuantityException extends RuntimeException {
    public GuestsQuantityException(String message) {
        super(message);
    }
}
