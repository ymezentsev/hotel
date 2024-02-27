package com.robot.hotel.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @FutureOrPresent(message = "Check in date must be future or present")
    private LocalDate checkInDate;

    @Future(message = "Check out date must be future")
    private LocalDate checkOutDate;

    private Set<String> guests;
}