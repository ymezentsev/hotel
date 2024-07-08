package com.robot.hotel.user;

import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserRequest;
import com.robot.hotel.user.dto.UserSearchParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> findAll(Pageable pageable);

    UserDto save(UserRequest userRequest);

    UserDto findById(Long id);

    UserDto findByEmail(String email);

    UserDto findByPhoneNumber(String phoneNumber);

    UserDto findByPassportSerialNumber(String passportSerialNumber);

    Page<UserDto> findByLastName(String lastName, Pageable pageable);

    Page<UserDto> findUsersByReservation(Long id, Pageable pageable);

    Page<UserDto> findUsersByRole(String role, Pageable pageable);

    Page<UserDto> search(UserSearchParameters params, Pageable pageable);

    void update(Long userId, UserRequest userRequest);

    void deleteById(Long id);
}