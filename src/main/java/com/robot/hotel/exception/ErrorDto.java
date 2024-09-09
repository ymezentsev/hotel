package com.robot.hotel.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorDto(
        Integer errorCode,
        String error,
        LocalDateTime timestamp,
        String errorDescription) {
}