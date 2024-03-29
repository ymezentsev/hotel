package com.robot.hotel.reservation;

import com.robot.hotel.user.UserDtoWithoutReservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long id;

    private String roomNumber;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private List<UserDtoWithoutReservation> users;
}
