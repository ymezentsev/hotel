package com.robot.hotel.user.controller;

import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> confirm(@RequestParam("token") String token) {
        HttpHeaders headers = new HttpHeaders();

/*        try {
            registrationService.confirmToken(token);
            headers.add("Location", frontendBaseUrl + LOGIN_PAGE_URL);
        } catch (EmailConfirmationTokenExpiredException e) {
            headers.add("Location",
                    frontendBaseUrl + AUTH_ERROR_PAGE_URL + e.getMessage().substring(14));
        } catch (EmailAlreadyConfirmedException e) {
            headers.add("Location", frontendBaseUrl + LOGIN_PAGE_URL);
        }*/

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
