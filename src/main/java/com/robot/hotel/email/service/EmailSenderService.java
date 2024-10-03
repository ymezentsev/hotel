package com.robot.hotel.email.service;

public interface EmailSenderService {

    void send(String to, String message, String subject);
}
