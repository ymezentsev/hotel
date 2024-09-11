package com.robot.hotel.user.service.impl;

import com.robot.hotel.country.CountryRepository;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NoSuchElementException;
import com.robot.hotel.exception.NotEnoughInformationException;
import com.robot.hotel.user.model.Passport;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.repository.PassportRepository;
import com.robot.hotel.user.repository.UserRepository;
import com.robot.hotel.user.service.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public Passport getPassportFromUserRequest(String passportSerialNumber,
                                               String countryCode,
                                               LocalDate issueDate,
                                               Long userId) {
        if (passportSerialNumber == null &&
                countryCode == null &&
                issueDate == null) {
            return null;
        }

        if (passportSerialNumber != null &&
                countryCode != null &&
                issueDate != null) {
            if (userId == null && passportRepository.existsBySerialNumber(
                    passportSerialNumber.toLowerCase().strip())) {
                throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "passport"));
            }

            if (userId != null) {
                Optional<User> existingUser = userRepository.findByPassportSerialNumber(
                        passportSerialNumber.toLowerCase().strip());
                if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                    throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "passport"));
                }
            }

            return Passport.builder()
                    .serialNumber(passportSerialNumber.toLowerCase().strip())
                    .country(countryRepository.findById(countryCode.toUpperCase())
                            .orElseThrow(() -> new NoSuchElementException(COUNTRY_IS_NOT_EXISTS)))
                    .issueDate(issueDate)
                    .build();
        }
        throw new NotEnoughInformationException(NOT_ENOUGH_INFORMATION);
    }
}
