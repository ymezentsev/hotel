package com.robot.hotel.user.service;

import com.robot.hotel.user.model.ConfirmationToken;
import com.robot.hotel.user.model.User;

public interface ConfirmationTokenService {
    String saveConfirmationToken(ConfirmationToken token);

    ConfirmationToken getConfirmationToken(String token);

    ConfirmationToken generateConfirmationToken(User user);

    void validateConfirmationToken(ConfirmationToken confirmationToken);
}
