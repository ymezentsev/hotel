package com.robot.hotel.user.service;

import com.robot.hotel.user.ConfirmationToken;
import com.robot.hotel.user.User;

public interface ConfirmationTokenService {
    String saveConfirmationToken(ConfirmationToken token);

    ConfirmationToken getToken(String token);

    ConfirmationToken generateToken(User user);
}
