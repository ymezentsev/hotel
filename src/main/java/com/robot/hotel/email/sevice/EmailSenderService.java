package com.robot.hotel.email.sevice;

public interface EmailSenderService {

    void send(String to, String message, String subject);
}
