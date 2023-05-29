package com.robot.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long id;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Set<String> guests;
}
