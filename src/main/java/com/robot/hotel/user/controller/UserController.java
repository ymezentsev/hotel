package com.robot.hotel.user.controller;

import com.robot.hotel.user.dto.EmailRequestDto;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserSearchParameters;
import com.robot.hotel.user.dto.password.ChangePasswordRequestDto;
import com.robot.hotel.user.dto.password.ForgotPasswordRequestDto;
import com.robot.hotel.user.service.UserService;
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

/*    @GetMapping("/reset-password")
    public void getResetPassword(@RequestParam("token") String token) {
        HttpHeaders headers = new HttpHeaders();

        try {
            forgotPasswordTokenService.validateForgotPasswordToken(token);
            headers.add("Location", frontendBaseUrl + TOKEN_CONFIRMATION_URL + token);
        } catch (ForgotConfirmationTokenExpiredException e) {
            headers.add("Location",
                    frontendBaseUrl + AUTH_ERROR_PAGE_URL + e.getMessage().substring(14));
        } catch (IllegalStateException e) {
            headers.add("Location", frontendBaseUrl + LOGIN_PAGE_URL);
        }

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }*/

    @PutMapping("/reset-password")
    //todo add tests
    public void resetPassword(@RequestBody @Valid ForgotPasswordRequestDto request,
                              @RequestParam("token") String token) {
        userService.forgotPassword(request.getNewPassword(), token);
    }

    @PutMapping("/change-password")
    //todo add tests
    public void changePassword(@RequestBody @Valid ChangePasswordRequestDto request) {
        userService.changePassword(request);
    }
}