package com.robot.hotel.user.service;

public interface EmailSenderService {

    void send(String to, String message, String subject);
}