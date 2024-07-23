package com.robot.hotel.user.service;

import com.robot.hotel.country.Country;
import com.robot.hotel.country.CountryService;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.FailedToSendEmailException;
import com.robot.hotel.user.EmailUtil;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.mapper.UserMapper;
import com.robot.hotel.user.model.ConfirmationToken;
import com.robot.hotel.user.model.Passport;
import com.robot.hotel.user.model.Role;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private static final String USER_IS_ALREADY_EXISTS = "User with such %s already exists";
    private static final String USER_IS_NOT_EXISTS = "User with email %s not exists";
    private static final String EMAIL_IS_ALREADY_VERIFIED = "User email is already verified";
    private static final String TOKEN_CONFIRMATION_URL = "/auth/confirm?token=";
    private static final String EMAIL_SUBJECT = "Email Confirmation";

    @Value("${backend.base.url}")
    private String backendBaseUrl;

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
                .password(registrationRequestDto.getPassword())
                .role(Role.USER)
                .passport(passport)
                .reservations(Collections.emptySet())
                .build();

        User savedUser = userRepository.save(newUser);
        log.info("Successful register user with id: {}", savedUser.getId());

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
        log.debug("Sending confirmation email to: {}", email);

        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new NoSuchElementException(String.format(USER_IS_NOT_EXISTS, email.toLowerCase())));

        if (user.isEnabled()) {
            throw new FailedToSendEmailException(EMAIL_IS_ALREADY_VERIFIED);
        }

        ConfirmationToken confirmationToken = confirmationTokenService.generateConfirmationToken(user);
        String token = confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = backendBaseUrl + TOKEN_CONFIRMATION_URL + token;
        emailSenderService.send(
                user.getEmail().toLowerCase(),
                EmailUtil.buildEmailTokenConfirmationMessage(user.getFirstName(), link),
                EMAIL_SUBJECT);
        log.info("Confirmation email sent successfully to: {}", email);
    }
}