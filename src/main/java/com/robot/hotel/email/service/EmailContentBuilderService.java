package com.robot.hotel.email.service;

import com.robot.hotel.email.EmailSubject;
import com.robot.hotel.reservation.Reservation;

public interface EmailContentBuilderService {
    String buildEmailContent(String name, String token, Reservation reservation, EmailSubject subject);
}
