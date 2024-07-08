package com.robot.hotel.user;

import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserRequest;
import com.robot.hotel.user.dto.UserSearchParameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserControllerOpenApi {
    private final UserService userService;

    @GetMapping()
    public Page<UserDto> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PostMapping()
    public UserDto save(@Valid @RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @GetMapping(value = "/{id}")
    public UserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/email/{email}")
    public UserDto findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/phoneNumber/{phoneNumber}")
    public UserDto findByPhoneNumber(@PathVariable String phoneNumber) {
        return userService.findByPhoneNumber(phoneNumber);
    }

    @GetMapping("/passport/{passportSerialNumber}")
    public UserDto findByPassportSerialNumber(@PathVariable String passportSerialNumber) {
        return userService.findByPassportSerialNumber(passportSerialNumber);
    }

    @GetMapping("/lastName/{lastName}")
    public Page<UserDto> findByLastName(@PathVariable String lastName, Pageable pageable) {
        return userService.findByLastName(lastName, pageable);
    }

    @GetMapping("/reservations/{id}")
    public Page<UserDto> findUsersByReservation(@PathVariable Long id, Pageable pageable) {
        return userService.findUsersByReservation(id, pageable);
    }

    @GetMapping("/role/{role}")
    public Page<UserDto> findUsersByRole(@PathVariable String role, Pageable pageable) {
        return userService.findUsersByRole(role, pageable);
    }

    @GetMapping("/search")
    public Page<UserDto> search(UserSearchParameters parameters, Pageable pageable) {
        return userService.search(parameters, pageable);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        userService.update(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}