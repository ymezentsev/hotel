package com.robot.hotel.user;

import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserControllerOpenApi {
    private final UserService userService;

    @GetMapping()
    public List<UserDto> findAll() {
        return userService.findAll();
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

    @GetMapping("/telNumber/{telNumber}")
    public UserDto findByTelNumber(@PathVariable String telNumber) {
        return userService.findByTelNumber(telNumber);
    }

    @GetMapping("/passport/{passportSerialNumber}")
    public UserDto findByPassportSerialNumber(@PathVariable String passportSerialNumber) {
        return userService.findByPassportSerialNumber(passportSerialNumber);
    }

    @GetMapping("/lastName/{lastName}")
    public List<UserDto> findByLastName(@PathVariable String lastName) {
        return userService.findByLastName(lastName);
    }

    @GetMapping("/reservations/{id}")
    public List<UserDto> findUsersByReservation(@PathVariable Long id) {
        return userService.findUsersByReservation(id);
    }

    @GetMapping("/role/{role}")
    public List<UserDto> findUsersByRole(@PathVariable String role) {
        return userService.findUsersByRole(role);
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