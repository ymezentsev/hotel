package com.robot.hotel.email_sender;

public interface EmailSenderService {

    void send(String to, String message, String subject);
}
