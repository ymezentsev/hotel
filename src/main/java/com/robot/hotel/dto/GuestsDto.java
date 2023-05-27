package com.robot.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String telNumber;
    private String email;
    private String passportSerialNumber;
    private Set<String> reservations;
}
