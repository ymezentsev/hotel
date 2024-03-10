package com.robot.hotel.user;

import com.robot.hotel.country.Country;
import com.robot.hotel.passport.Passport;
import com.robot.hotel.passport.PassportMapper;
import com.robot.hotel.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final PassportMapper passportMapper;

    public UserDto buildUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .telNumber(user.getCountry().getTelCode() + user.getTelNumber())
                .email(user.getEmail())
                .role(user.getRole().name())
                .passport(passportMapper.buildPassportDto(user.getPassport()))
                .reservations(user.getReservations().stream()
                        .map(getReservationsString())
                        .toList())
                .build();
    }

    public User buildUserFromRequest(UserRequest userRequest, Country country, Passport passport) {
        return User.builder()
                .firstName(userRequest.getFirstName().toLowerCase())
                .lastName(userRequest.getLastName().toLowerCase())
                .country(country)
                .telNumber(userRequest.getTelNumber())
                .email(userRequest.getEmail().toLowerCase())
                .password(userRequest.getPassword())
                .role(Role.USER)
                .passport(passport)
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
