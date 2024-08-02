package com.robot.hotel.user.dto;

public record UserSearchParametersDto(String[] firstnames, String[] lastnames,
                                      String[] phoneNumbers, String[] emails,
                                      String[] roles, String[] passportSerialNumbers,
                                      String[] reservations, String[] countryCodes) {
}
