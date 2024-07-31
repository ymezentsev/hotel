package com.robot.hotel.user.controller;

import com.robot.hotel.user.dto.EmailRequestDto;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserSearchParameters;
import com.robot.hotel.user.dto.password.ChangePasswordRequestDto;
import com.robot.hotel.user.dto.password.ForgotPasswordRequestDto;
import com.robot.hotel.user.service.ForgotPasswordTokenService;
import com.robot.hotel.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserControllerOpenApi {
    private final UserService userService;
    private final ForgotPasswordTokenService forgotPasswordTokenService;

    @GetMapping()
    public Page<UserDto> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public UserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/search")
    public Page<UserDto> search(UserSearchParameters parameters, Pageable pageable) {
        return userService.search(parameters, pageable);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody RegistrationRequestDto registrationRequestDto) {
        userService.update(id, registrationRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        userService.sendForgotPasswordEmail(emailRequestDto.getEmail());
    }

    @GetMapping("/reset-password")
    //todo add page for set new password
    public ResponseEntity<Void> getResetPassword(@RequestParam("token") String token) {
        HttpHeaders headers = new HttpHeaders();

        forgotPasswordTokenService.validateForgotPasswordToken(
                forgotPasswordTokenService.getForgotPasswordToken(token));
       // headers.add("Location", frontendBaseUrl + TOKEN_RESET_PASSWORD_URL + token);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PutMapping("/reset-password")
    //todo add tests
    public void resetPassword(@RequestBody @Valid ForgotPasswordRequestDto request,
                              @RequestParam("token") String token) {
        userService.resetPassword(request.getNewPassword(), token);
    }

    @PutMapping("/change-password")
    //todo add tests
    public void changePassword(@RequestBody @Valid ChangePasswordRequestDto request) {
        userService.changePassword(request);
    }
}