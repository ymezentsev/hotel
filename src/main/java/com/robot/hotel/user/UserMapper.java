package com.robot.hotel.user;

import com.robot.hotel.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserMapper {
    public UserDto buildUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .telNumber(user.getTelNumber())
                .email(user.getEmail())
                //.passportSerialNumber(user.getPassportSerialNumber())
                .reservations(user.getReservations().stream()
                        .map(getReservationsString())
                        .toList())
                .build();
    }

    public User buildUserFromRequest(UserRequest userRequest) {
        if (Objects.isNull(userRequest.getPassportSerialNumber())) {
            userRequest.setPassportSerialNumber("");
        }

        return User.builder()
                .firstName(userRequest.getFirstName().toLowerCase().strip())
                .lastName(userRequest.getLastName().toLowerCase().strip())
                .telNumber(userRequest.getTelNumber().strip())
                .email(userRequest.getEmail().toLowerCase().strip())
               // .passportSerialNumber(userRequest.getPassportSerialNumber().toLowerCase().strip())
                .reservations(Collections.emptyList())
                .build();
    }

    private Function<Reservation, String> getReservationsString() {
        return reservation -> "id:" + reservation.getId().toString()
                + ", room:" + reservation.getRoom().getNumber()
                + ", " + reservation.getCheckInDate().toString() + " - "
                + reservation.getCheckOutDate().toString();
    }
}
