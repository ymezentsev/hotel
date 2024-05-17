package com.robot.hotel.user;

import com.robot.hotel.reservation.dto.ReservationDtoWithoutUserInfo;
import com.robot.hotel.user.passport.PassportDto;
import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(Long id, String firstName, String lastName, String telNumber,
                      String email, String role, PassportDto passport,
                      List<ReservationDtoWithoutUserInfo> reservations) {
}