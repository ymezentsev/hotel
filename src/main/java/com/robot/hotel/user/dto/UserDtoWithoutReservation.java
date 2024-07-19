package com.robot.hotel.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto for User without Reservation")
public record UserDtoWithoutReservation(Long id, String firstName, String lastName,
                                        String phoneNumber, String email, PassportDto passport) {
}