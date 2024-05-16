package com.robot.hotel.room.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for get free rooms")
public class FreeRoomRequest {
    @NotNull(message = "Check in date is required")
    @FutureOrPresent(message = "Check in date must be future or present")
    private LocalDate checkInDate;

    @NotNull(message = "Check out date is required")
    @Future(message = "Check out date must be future")
    private LocalDate checkOutDate;
}