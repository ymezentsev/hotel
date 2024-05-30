package com.robot.hotel.reservation.dto;

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
    @Schema(description = "Room number", example = "101")
    private String roomNumber;

    @NotNull(message = "Check in date is required")
    @FutureOrPresent(message = "Check in date must be future or present")
    @Schema(description = "Check in date", example = "2024-01-01")
    private LocalDate checkInDate;

    @NotNull(message = "Check out date is required")
    @Future(message = "Check out date must be future")
    @Schema(description = "Check out date", example = "2024-01-10")
    private LocalDate checkOutDate;

    @Size(min = 1, message = "User email is required")
    @Schema(description = "User's emails", example = "[\"johndoe@gmail.com\", \"marksmith@gmail.com\"]")
    private Set<String> userEmails;
}