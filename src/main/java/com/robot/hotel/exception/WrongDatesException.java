package com.robot.hotel.exception;

public class WrongDatesException extends RuntimeException {
    public WrongDatesException(String message) {
        super(message);
    }
}