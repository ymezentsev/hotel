package com.robot.hotel.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for create reservation")
public class ReservationRequest {
    @NotBlank(message = "Room number is required")
    @Size(max = 20, message = "Max size for room number is 20 characters")
    private String roomNumber;

    @NotNull(message = "Check in date is required")
    @FutureOrPresent(message = "Check in date must be future or present")
    private LocalDate checkInDate;

    @NotNull(message = "Check out date is required")
    @Future(message = "Check out date must be future")
    private LocalDate checkOutDate;

    @Size(min = 1, message = "User id is required")
    private Set<String> users;
}