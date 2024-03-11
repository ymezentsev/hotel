package com.robot.hotel.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDtoWithoutUserInfo {
    private Long id;

    private String roomNumber;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer numberOfGuests;
}
