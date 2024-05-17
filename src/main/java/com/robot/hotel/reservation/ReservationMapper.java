package com.robot.hotel.reservation;

import com.robot.hotel.reservation.dto.ReservationDto;
import com.robot.hotel.reservation.dto.ReservationDtoWithoutUserInfo;
import com.robot.hotel.reservation.dto.ReservationRequest;
import com.robot.hotel.room.Room;
import com.robot.hotel.user.User;
import com.robot.hotel.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationMapper {
    @Lazy
    @Autowired
    UserMapper userMapper;

    public Reservation buildReservationFromRequest(ReservationRequest reservationRequest, Room room, List<User> users) {
        return Reservation.builder()
                .room(room)
                .checkInDate(reservationRequest.getCheckInDate())
                .checkOutDate(reservationRequest.getCheckOutDate())
                .users(users)
                .build();
    }

    public ReservationDto buildReservationDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .roomNumber(reservation.getRoom().getNumber())
                .users(reservation.getUsers().stream()
                        .map(userMapper::buildUserDtoWithoutReservation)
                        .toList())
                .build();
    }

    public ReservationDtoWithoutUserInfo buildReservationDtoWithoutUserInfo(Reservation reservation) {
        return ReservationDtoWithoutUserInfo.builder()
                .id(reservation.getId())
                .checkInDate(reservation.getCheckInDate())
                .checkOutDate(reservation.getCheckOutDate())
                .roomNumber(reservation.getRoom().getNumber())
                .numberOfGuests(reservation.getUsers().size())
                .build();
    }
}
