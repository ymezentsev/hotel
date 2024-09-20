package com.robot.hotel.email;

import com.robot.hotel.reservation.Reservation;

public interface EmailContentBuilderService {
    String buildEmailContent(String name, String token, Reservation reservation, EmailSubject subject);
}
