package com.robot.hotel.user;

import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;

public interface RegistrationService {

    UserDto register(RegistrationRequestDto registrationRequestDto);

    void confirmToken(String token);

    void sendConfirmationEmail(String email);
}