package com.robot.hotel.user;

import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserRequest;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto save(UserRequest userRequest);

    UserDto findById(Long id);

    UserDto findByEmail(String email);

    UserDto findByTelNumber(String telNumber);

    UserDto findByPassportSerialNumber(String passportSerialNumber);

    List<UserDto> findByLastName(String lastName);

    List<UserDto> findUsersByReservation(Long id);

    List<UserDto> findUsersByRole(String role);

    void update(Long userId, UserRequest userRequest);

    void deleteById(Long id);
}
