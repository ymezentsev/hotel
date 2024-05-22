package com.robot.hotel.reservation.dto;

import com.robot.hotel.room.dto.RoomDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dto for Reservation without user info")
public record ReservationDtoWithoutUserInfo(Long id, RoomDto room,
                                            LocalDate checkInDate, LocalDate checkOutDate,
                                            Integer numberOfGuests) {
}