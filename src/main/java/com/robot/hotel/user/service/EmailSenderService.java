package com.robot.hotel.user.service;

import com.robot.hotel.user.model.EmailSubject;

public interface EmailSenderService {

    void send(String to, String message, String subject);

    String buildEmailContent(String name, String token, EmailSubject subject);
}
