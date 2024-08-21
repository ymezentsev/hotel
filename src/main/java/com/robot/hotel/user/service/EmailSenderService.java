package com.robot.hotel.user.service;

import com.robot.hotel.user.model.enums.EmailSubject;

public interface EmailSenderService {

    void send(String to, String message, String subject);
}
