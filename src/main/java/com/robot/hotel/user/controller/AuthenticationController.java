package com.robot.hotel.user.controller;

import com.robot.hotel.user.dto.EmailRequestDto;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody RegistrationRequestDto registrationRequestDto) {
        return registrationService.register(registrationRequestDto);
    }

    @GetMapping("/confirm")
    public void confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
    }

    @PostMapping("/resend-email-confirmation")
    public void resendConfirmationEmail(@RequestBody EmailRequestDto emailRequestDto) {
        registrationService.sendConfirmationEmail(emailRequestDto.getEmail());
    }
}
