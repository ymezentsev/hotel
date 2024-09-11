package com.robot.hotel.user.service;

import com.robot.hotel.user.dto.UpdateUserRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserSearchParametersDto;
import com.robot.hotel.user.dto.password.ChangePasswordRequestDto;
import com.robot.hotel.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> findAll(Pageable pageable);

    UserDto findById(Long id);

    Page<UserDto> search(UserSearchParametersDto params, Pageable pageable);

    void update(Long userId, UpdateUserRequestDto updateUserRequestDto);

    void deleteById(Long id);

    void enableUser(User user);

    void sendForgotPasswordEmail(String email);

    void resetPassword(String newPassword, String token);

    void changePassword(ChangePasswordRequestDto request);

    User getCurrentAuthenticatedUser();
}