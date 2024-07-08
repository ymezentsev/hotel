package com.robot.hotel.user.dto;

public record UserSearchParameters(String[] firstnames, String[] lastnames,
                                   String[] phoneNumbers, String[] emails,
                                   String[] roles, String[] passportSerialNumbers,
                                   String[] reservations, String[] countryCodes) {
}
