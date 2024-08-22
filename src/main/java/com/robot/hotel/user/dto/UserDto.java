package com.robot.hotel.user.dto;

import com.robot.hotel.reservation.dto.ReservationDtoWithoutUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Dto for User")
public record UserDto(Long id, String firstName, String lastName, String phoneNumber,
                      String email, List<RoleDto> roles, PassportDto passport,
                      List<ReservationDtoWithoutUserInfo> reservations, Boolean isEnabled) {
}