package com.robot.hotel.user.service;

import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.model.Passport;

public interface PassportService {
    Passport getPassportFromUserRequest(RegistrationRequestDto registrationRequestDto, Long userId);
}
