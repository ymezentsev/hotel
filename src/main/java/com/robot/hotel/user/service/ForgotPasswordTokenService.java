package com.robot.hotel.user.service;

import com.robot.hotel.user.model.ForgotPasswordToken;
import com.robot.hotel.user.model.User;

public interface ForgotPasswordTokenService {
    String saveForgotPasswordToken(ForgotPasswordToken token);

    ForgotPasswordToken getForgotPasswordToken(String token);

    ForgotPasswordToken generateForgotPasswordToken(User user);

    void validateForgotPasswordToken(ForgotPasswordToken forgotPasswordToken);
}
