package com.robot.hotel.reservation;

import com.robot.hotel.user.UserDtoWithoutReservation;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ReservationDto(Long id, String roomNumber,
                             LocalDate checkInDate, LocalDate checkOutDate,
                             List<UserDtoWithoutReservation> users) {
}