package com.robot.hotel.user;

import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.user.country.Country;
import com.robot.hotel.user.country.CountryService;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.passport.Passport;
import com.robot.hotel.user.passport.PassportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService{
    private final UserRepository userRepository;
    private final CountryService countryService;
    private final PassportService passportService;
    private final UserMapper userMapper;

    private static final String USER_IS_ALREADY_EXISTS = "User with such %s is already exists";
    private static final String SUCCESSFUL_ACTION_WITH_USER = "Successful %s user with id: {}";

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
        log.info(String.format(SUCCESSFUL_ACTION_WITH_USER, "created"), savedUser.getId());

        sendConfirmationEmail(registrationRequestDto.getEmail());
        return userMapper.toDto(savedUser);
    }

    @Override
    public void confirmToken(String token) {

    }

    @Override
    public void sendConfirmationEmail(String email) {

    }
}
