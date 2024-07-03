package com.robot.hotel.exception;

public class DuplicateObjectException extends RuntimeException {
    public DuplicateObjectException(String message) {
        super(message);
    }
}