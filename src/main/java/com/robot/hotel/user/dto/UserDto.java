package com.robot.hotel.user.dto;

import com.robot.hotel.reservation.dto.ReservationDtoWithoutUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

//todo delete role from dtos
@Schema(description = "Dto for User")
public record UserDto(Long id, String firstName, String lastName, String phoneNumber,
                      String email, String role, PassportDto passport,
                      List<ReservationDtoWithoutUserInfo> reservations, boolean isEnabled) {
}