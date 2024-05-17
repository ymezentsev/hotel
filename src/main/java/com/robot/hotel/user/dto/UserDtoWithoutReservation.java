package com.robot.hotel.user.dto;

import com.robot.hotel.user.passport.PassportDto;
import lombok.Builder;

@Builder
public record UserDtoWithoutReservation(Long id, String firstName, String lastName,
                                        String telNumber, String email, PassportDto passport) {
}