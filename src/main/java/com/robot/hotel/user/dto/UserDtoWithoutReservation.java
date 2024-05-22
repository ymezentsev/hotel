package com.robot.hotel.user.dto;

import com.robot.hotel.user.passport.PassportDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto for User without Reservation")
public record UserDtoWithoutReservation(Long id, String firstName, String lastName,
                                        String phoneNumber, String email, PassportDto passport) {
}