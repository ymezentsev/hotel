package com.robot.hotel.exception;

public class FailedToSendEmailException extends RuntimeException {
    public FailedToSendEmailException(String message) {
        super(message);
    }
}