package com.robot.hotel.user.service;

import com.robot.hotel.country.Country;
import com.robot.hotel.country.CountryService;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.FailedToSendEmailException;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.mapper.UserMapper;
import com.robot.hotel.user.model.*;
import com.robot.hotel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserService userService;
    private final CountryService countryService;
    private final PassportService passportService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_IS_ALREADY_EXISTS = "User with such %s already exists";
    private static final String USER_IS_NOT_EXISTS = "User with email %s not exists";
    private static final String EMAIL_IS_ALREADY_VERIFIED = "User email is already verified";

    @Override
    public UserDto register(RegistrationRequestDto registrationRequestDto) {
        log.info("Register user with email: {}", registrationRequestDto.getEmail().toLowerCase());
        Country country = countryService.getCountryFromPhoneCode(registrationRequestDto.getPhoneCode());

        if (userRepository.existsByPhoneNumber(registrationRequestDto.getPhoneNumber())) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "phone number"));
        }

        if (userRepository.existsByEmail(registrationRequestDto.getEmail().toLowerCase())) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "email"));
        }

        Passport passport = passportService.getPassportFromUserRequest(registrationRequestDto, null);

        User newUser = User.builder()
                .firstName(registrationRequestDto.getFirstName().toLowerCase())
                .lastName(registrationRequestDto.getLastName().toLowerCase())
                .country(country)
                .phoneNumber(registrationRequestDto.getPhoneNumber())
                .email(registrationRequestDto.getEmail().toLowerCase())
                .password(passwordEncoder.encode(registrationRequestDto.getPassword()))
                .role(Role.USER)
                .passport(passport)
                .reservations(Collections.emptySet())
                .isEnabled(false)
                .build();

        User savedUser = userRepository.save(newUser);
        log.info("Successful registered user with id: {}", savedUser.getId());

        sendConfirmationEmail(registrationRequestDto.getEmail());
        return userMapper.toDto(savedUser);
    }

    @Override
    public void confirmToken(String token) {
        log.info("Confirm user by token: {}", token);
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationToken(token);
        confirmationTokenService.validateConfirmationToken(confirmationToken);

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        userService.enableUser(confirmationToken.getUser());
        log.info("User with email: {} has successful confirmed", confirmationToken.getUser().getEmail());
    }

    @Override
    public void sendConfirmationEmail(String email) {
        log.info("Sending confirmation email to: {}", email);

        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new NoSuchElementException(String.format(USER_IS_NOT_EXISTS, email.toLowerCase())));

        if (user.isEnabled()) {
            throw new FailedToSendEmailException(EMAIL_IS_ALREADY_VERIFIED);
        }

        ConfirmationToken confirmationToken = confirmationTokenService.generateConfirmationToken(user);
        String token = confirmationTokenService.saveConfirmationToken(confirmationToken);

        emailSenderService.send(
                user.getEmail().toLowerCase(),
                emailSenderService.buildEmailContent(user.getFirstName(), token, EmailSubject.CONFIRM_EMAIL),
                EmailSubject.CONFIRM_EMAIL.getSubject());
        log.info("Confirmation email sent successfully to: {}", email);
    }
}
