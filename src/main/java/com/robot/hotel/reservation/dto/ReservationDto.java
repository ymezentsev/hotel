package com.robot.hotel.reservation.dto;

import com.robot.hotel.room.dto.RoomDto;
import com.robot.hotel.user.dto.UserDtoWithoutReservation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Dto for Reservation")
public record ReservationDto(Long id, RoomDto room,
                             LocalDate checkInDate, LocalDate checkOutDate,
                             List<UserDtoWithoutReservation> users) {
}