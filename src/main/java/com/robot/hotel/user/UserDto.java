package com.robot.hotel.user;

import com.robot.hotel.user.passport.PassportDto;
import com.robot.hotel.reservation.ReservationDtoWithoutUserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String telNumber;

    private String email;

    private String role;

    private PassportDto passport;

    private List<ReservationDtoWithoutUserInfo> reservations;
}
