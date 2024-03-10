package com.robot.hotel.reservation;

import com.robot.hotel.user.User;
import com.robot.hotel.room.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReservationMapper {
    public ReservationDto buildReservationDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .roomNumber(reservation.getRoom().getNumber())
                .users(reservation.getUsers().stream()
                        .map(getUserString())
                        .toList())
                .build();
    }

    public Reservation buildReservationFromRequest(ReservationRequest reservationRequest, Room room, List<User> users) {
        return Reservation.builder()
                .room(room)
                .checkInDate(reservationRequest.getCheckInDate())
                .checkOutDate(reservationRequest.getCheckOutDate())
                .users(users)
                .build();
    }

    private Function<User, String> getUserString() {
        return user -> "id:" + user.getId().toString()
                + ", " + user.getFirstName() + " " + user.getLastName()
                + ", " + user.getCountry().getTelCode() + user.getTelNumber()
                + ", " + user.getEmail();
    }
}
