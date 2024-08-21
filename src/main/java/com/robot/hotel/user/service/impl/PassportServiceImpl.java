package com.robot.hotel.user.service.impl;

import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEnoughInformationException;
import com.robot.hotel.user.model.Passport;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.repository.PassportRepository;
import com.robot.hotel.user.repository.UserRepository;
import com.robot.hotel.country.CountryRepository;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.service.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {
    private final PassportRepository passportRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;

    private static final String USER_IS_ALREADY_EXISTS = "User with such %s is already exists";
    private static final String COUNTRY_IS_NOT_EXISTS = "Such country is not exists";
    private static final String NOT_ENOUGH_INFORMATION = "There is not enough information to save your passport";

    @Override
    public Passport getPassportFromUserRequest(RegistrationRequestDto registrationRequestDto, Long userId) {
        if (registrationRequestDto.getPassportSerialNumber() == null &&
                registrationRequestDto.getCountryCode() == null &&
                registrationRequestDto.getIssueDate() == null) {
            return null;
        }

        if (registrationRequestDto.getPassportSerialNumber() != null &&
                registrationRequestDto.getCountryCode() != null &&
                registrationRequestDto.getIssueDate() != null) {
            if (userId == null && passportRepository.existsBySerialNumber(
                    registrationRequestDto.getPassportSerialNumber().toLowerCase().strip())) {
                throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "passport"));
            }

            if (userId != null) {
                Optional<User> existingUser = userRepository.findByPassportSerialNumber(
                        registrationRequestDto.getPassportSerialNumber().toLowerCase().strip());
                if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                    throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "passport"));
                }
            }

            return Passport.builder()
                    .serialNumber(registrationRequestDto.getPassportSerialNumber().toLowerCase().strip())
                    .country(countryRepository.findById(registrationRequestDto.getCountryCode().toUpperCase())
                            .orElseThrow(() -> new NoSuchElementException(COUNTRY_IS_NOT_EXISTS)))
                    .issueDate(registrationRequestDto.getIssueDate())
                    .build();
        }
        throw new NotEnoughInformationException(NOT_ENOUGH_INFORMATION);
    }
}
