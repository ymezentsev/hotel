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

    Page<UserDto> search(UserSearchParameters params, Pageable pageable);

    void update(Long userId, UserRequest userRequest);

    void deleteById(Long id);
}