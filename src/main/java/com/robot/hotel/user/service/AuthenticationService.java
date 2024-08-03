package com.robot.hotel.user.service;

import com.robot.hotel.user.dto.login.LoginRequestDto;
import com.robot.hotel.user.dto.login.LoginResponseDto;

public interface AuthenticationService {
    LoginResponseDto authenticate(LoginRequestDto request);
}
