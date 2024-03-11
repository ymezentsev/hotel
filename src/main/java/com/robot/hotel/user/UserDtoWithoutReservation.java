package com.robot.hotel.user;

import com.robot.hotel.passport.PassportDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoWithoutReservation {
    private Long id;

    private String firstName;

    private String lastName;

    private String telNumber;

    private String email;

    private PassportDto passport;
}
