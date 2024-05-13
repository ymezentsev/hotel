package com.robot.hotel.user;

import com.robot.hotel.user.country.Country;
import com.robot.hotel.user.passport.Passport;
import com.robot.hotel.user.passport.PassportMapper;
import com.robot.hotel.reservation.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final PassportMapper passportMapper;
    private final ReservationMapper reservationMapper;

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
                        .map(reservationMapper::buildReservationDtoWithoutUserInfo)
                        .toList())
                .build();
    }

    public UserDtoWithoutReservation buildUserDtoWithoutReservation(User user) {
        return UserDtoWithoutReservation.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .telNumber(user.getCountry().getTelCode() + user.getTelNumber())
                .email(user.getEmail())
                .passport(passportMapper.buildPassportDto(user.getPassport()))
                .build();
    }
}
