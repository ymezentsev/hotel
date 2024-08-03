package com.robot.hotel.user.controller;

import com.robot.hotel.user.dto.EmailRequestDto;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.login.LoginRequestDto;
import com.robot.hotel.user.dto.login.LoginResponseDto;
import com.robot.hotel.user.service.AuthenticationService;
import com.robot.hotel.user.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody RegistrationRequestDto registrationRequestDto) {
        return registrationService.register(registrationRequestDto);
    }

    @GetMapping("/confirm")
    public void confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
    }

    @PostMapping("/resend-email-confirmation")
    public void resendConfirmationEmail(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        registrationService.sendConfirmationEmail(emailRequestDto.getEmail());
    }

    //todo add tests
    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
