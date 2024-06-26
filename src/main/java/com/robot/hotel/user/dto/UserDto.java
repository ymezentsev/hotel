package com.robot.hotel.user.dto;

import com.robot.hotel.reservation.dto.ReservationDtoWithoutUserInfo;
import com.robot.hotel.user.passport.PassportDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Dto for User")
public record UserDto(Long id, String firstName, String lastName, String phoneNumber,
                      String email, String role, PassportDto passport,
                      List<ReservationDtoWithoutUserInfo> reservations) {
}