package com.robot.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class NotEmptyObjectException extends RuntimeException {
    public NotEmptyObjectException(String message) {
        super(message);
    }
}
