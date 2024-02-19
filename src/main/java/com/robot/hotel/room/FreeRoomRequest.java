package com.robot.hotel.room;

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
    @FutureOrPresent(message = "Check in date must be future or present")
    LocalDate checkInDate;

    @Future(message = "Check out date must be future")
    LocalDate checkOutDate;
}
