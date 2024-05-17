package com.robot.hotel.reservation.dto;

import com.robot.hotel.user.dto.UserDtoWithoutReservation;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ReservationDto(Long id, String roomNumber,
                             LocalDate checkInDate, LocalDate checkOutDate,
                             List<UserDtoWithoutReservation> users) {
}