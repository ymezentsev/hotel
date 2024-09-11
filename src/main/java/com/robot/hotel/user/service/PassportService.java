package com.robot.hotel.user.service;

import com.robot.hotel.user.model.Passport;

import java.time.LocalDate;

public interface PassportService {
    Passport getPassportFromUserRequest(String passportSerialNumber,
                                        String countryCode,
                                        LocalDate issueDate,
                                        Long userId);
}
