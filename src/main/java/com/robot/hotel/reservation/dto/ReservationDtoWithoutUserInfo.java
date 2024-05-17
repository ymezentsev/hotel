package com.robot.hotel.reservation.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ReservationDtoWithoutUserInfo(Long id, String roomNumber,
                                            LocalDate checkInDate, LocalDate checkOutDate,
                                            Integer numberOfGuests) {
}