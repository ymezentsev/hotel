package com.robot.hotel.exception;

public class ConfirmationTokenExpiredException extends RuntimeException {
    public ConfirmationTokenExpiredException(String message) {
        super(message);
    }
}