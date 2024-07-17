package com.robot.hotel.user.passport;

import com.robot.hotel.user.dto.RegistrationRequestDto;

public interface PassportService {
    Passport getPassportFromUserRequest(RegistrationRequestDto registrationRequestDto, Long userId);
}
