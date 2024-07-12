package com.robot.hotel.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppError {
    private int errorCode;
    private LocalDateTime timestamp;
    private String errorText;
}