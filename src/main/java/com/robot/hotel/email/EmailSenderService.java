package com.robot.hotel.email;

public interface EmailSenderService {

    void send(String to, String message, String subject);
}
