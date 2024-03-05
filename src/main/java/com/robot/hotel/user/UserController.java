package com.robot.hotel.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users", description = "API to work with Users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    @Operation(summary = "Get all users")
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @PostMapping()
    @Operation(summary = "Create new user")
    public UserDto save(@Valid @RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public UserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email")
    public UserDto findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/telNumber/{telNumber}")
    @Operation(summary = "Get user by tel. number")
    public UserDto findByTelNumber(@PathVariable String telNumber) {
        return userService.findByTelNumber(telNumber);
    }

    @GetMapping("/passport/{passportSerialNumber}")
    @Operation(summary = "Get user by passport number")
    public UserDto findByPassportSerialNumber(@PathVariable String passportSerialNumber) {
        return userService.findByPassportSerialNumber(passportSerialNumber);
    }

    @GetMapping("/lastName/{lastName}")
    @Operation(summary = "Get users by lastname")
    public List<UserDto> findByLastName(@PathVariable String lastName) {
        return userService.findByLastName(lastName);
    }

    @GetMapping("/reservations/{id}")
    @Operation(summary = "Get users by reservation")
    public List<UserDto> findUsersByReservation(@PathVariable Long id) {
        return userService.findUsersByReservation(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public void update(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        userService.update(id, userRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
