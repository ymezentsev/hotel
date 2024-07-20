package com.robot.hotel.exception;

public class ConfirmationTokenAlreadyConfirmedException extends RuntimeException {
    public ConfirmationTokenAlreadyConfirmedException(String message) {
        super(message);
    }
}