package com.robot.hotel.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailSubject {
    CONFIRM_EMAIL("Email confirmation"),
    FORGOT_PASSWORD("Reset password instruction"),
    RESERVATION_CONFIRMATION("Reservation confirmation"),
    RESERVATION_REMINDER("Reservation reminder");

    private final String subject;
}