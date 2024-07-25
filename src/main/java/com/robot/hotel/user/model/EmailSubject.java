package com.robot.hotel.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailSubject {
    CONFIRM_EMAIL("Email confirmation"),
    FORGOT_PASSWORD("Reset password instruction");

    private final String subject;
}