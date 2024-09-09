package com.robot.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateObjectException extends RuntimeException {
    public DuplicateObjectException(String message) {
        super(message);
    }
}