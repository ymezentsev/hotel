package com.robot.hotel.user;

import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserSearchParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> findAll(Pageable pageable);

    UserDto findById(Long id);

    Page<UserDto> search(UserSearchParameters params, Pageable pageable);

    void update(Long userId, RegistrationRequestDto registrationRequestDto);

    void deleteById(Long id);
}