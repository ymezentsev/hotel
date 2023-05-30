package com.robot.hotel.exception;

public class NotEmptyObjectException extends RuntimeException {
    public NotEmptyObjectException(String message) {
        super(message);
    }
}
