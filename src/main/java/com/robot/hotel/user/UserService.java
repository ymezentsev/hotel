package com.robot.hotel.user;

import com.robot.hotel.country.Country;
import com.robot.hotel.country.CountryRepository;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.NotEnoughInformationException;
import com.robot.hotel.passport.Passport;
import com.robot.hotel.passport.PassportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CountryRepository countryRepository;
    private final PassportRepository passportRepository;

    private static final String USER_IS_ALREADY_EXISTS = "User with such %s is already exists";
    private static final String USER_IS_NOT_EXISTS = "Such user is not exists";
    private static final String RESERVATIONS_FOR_THIS_USER_ARE_EXISTS =
            "This user has reservations. At first delete reservations";
    private static final String TEL_CODE_IS_NOT_EXISTS = "Such tel. code is not exists";
    private static final String COUNTRY_IS_NOT_EXISTS = "Such country is not exists";
    private static final String NOT_ENOUGH_INFORMATION = "There is not enough information to save your passport";
    private static final String ROLE_IS_NOT_EXISTS = "Such role is not exists";

    @Transactional
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::buildUserDto)
                .toList();
    }

    public UserDto save(UserRequest userRequest) {
        Country country = getCountryFromTelCode(userRequest.getTelCode());

        if (userRepository.existsByTelNumber(userRequest.getTelNumber())) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "tel.number"));
        }

        if (userRepository.existsByEmail(userRequest.getEmail().toLowerCase())) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "email"));
        }

        Passport passport = getPassportFromUserRequest(userRequest, null);

        User newUser = userMapper.buildUserFromRequest(userRequest, country, passport);
        return userMapper.buildUserDto(userRepository.save(newUser));
    }

    @Transactional
    public UserDto findById(Long id) {
        return userMapper.buildUserDto(userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
    }

    @Transactional
    public UserDto findByEmail(String email) {
        return userMapper.buildUserDto(userRepository
                .findByEmail(email.toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
    }

    @Transactional
    public UserDto findByTelNumber(String telNumber) {
        if (telNumber.startsWith("+")) {
            return userMapper.buildUserDto(userRepository
                    .findByFullTelNumber(telNumber)
                    .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
        } else {
            return userMapper.buildUserDto(userRepository
                    .findByTelNumber(telNumber)
                    .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
        }
    }

    @Transactional
    public UserDto findByPassportSerialNumber(String passportSerialNumber) {
        return userMapper.buildUserDto(userRepository
                .findByPassportSerialNumber(passportSerialNumber.toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
    }

    @Transactional
    public List<UserDto> findByLastName(String lastName) {
        return userRepository.findByLastName(lastName.toLowerCase().strip()).stream()
                .map(userMapper::buildUserDto)
                .toList();
    }

    @Transactional
    public List<UserDto> findUsersByReservation(Long id) {
        return userRepository.findByReservationsId(id).stream()
                .map(userMapper::buildUserDto)
                .toList();
    }

    @Transactional
    public List<UserDto> findUsersByRole(String role) {
        role = role.toUpperCase().strip();

        if (!Arrays.stream(Role.values())
                .map(Enum::name)
                .toList()
                .contains(role)) {
            throw new NoSuchElementException(ROLE_IS_NOT_EXISTS);
        }

        return userRepository.findByRole(Role.valueOf(role)).stream()
                .map(userMapper::buildUserDto)
                .toList();
    }

    public void update(Long userId, UserRequest userRequest) {
        User userToUpdate = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException(USER_IS_NOT_EXISTS)
        );

        Country country = getCountryFromTelCode(userRequest.getTelCode());

        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail().toLowerCase());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "email"));
        }

        existingUser = userRepository.findByTelNumber(userRequest.getTelNumber());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "tel.number"));
        }

        Passport passport = getPassportFromUserRequest(userRequest, userId);

        userToUpdate.setFirstName(userRequest.getFirstName().toLowerCase());
        userToUpdate.setLastName(userRequest.getLastName().toLowerCase());
        userToUpdate.setCountry(country);
        userToUpdate.setTelNumber(userRequest.getTelNumber());
        userToUpdate.setEmail(userRequest.getEmail().toLowerCase());
        userToUpdate.setPassword(userRequest.getPassword());

        if (passport != null && !userToUpdate.getPassport().getSerialNumber().equals(passport.getSerialNumber())) {
            userToUpdate.setPassport(passport);
        }
        userRepository.save(userToUpdate);
    }

    public void deleteById(Long id) {
        if (userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS))
                .getReservations()
                .isEmpty()) {
            userRepository.deleteById(id);
        } else {
            throw new NotEmptyObjectException(RESERVATIONS_FOR_THIS_USER_ARE_EXISTS);
        }
    }

    private Country getCountryFromTelCode(String telCode) {
        List<Country> countries = countryRepository.findByTelCode(telCode);
        if (countries.isEmpty()) {
            throw new NoSuchElementException(TEL_CODE_IS_NOT_EXISTS);
        }
        return countries.get(0);
    }

    private Passport getPassportFromUserRequest(UserRequest userRequest, Long userId) {
        if (userRequest.getPassportSerialNumber() != null &&
                userRequest.getCountryCode() != null &&
                userRequest.getIssueDate() != null) {
            if (userId == null && passportRepository.existsBySerialNumber(
                    userRequest.getPassportSerialNumber().toLowerCase().strip())) {
                throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "passport"));
            }

            if (userId != null) {
                Optional<User> existingUser = userRepository.findByPassportSerialNumber(
                        userRequest.getPassportSerialNumber().toLowerCase().strip());
                if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                    throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "passport"));
                }
            }

            return Passport.builder()
                    .serialNumber(userRequest.getPassportSerialNumber().toLowerCase().strip())
                    .country(countryRepository.findById(userRequest.getCountryCode().toUpperCase())
                            .orElseThrow(() -> new NoSuchElementException(COUNTRY_IS_NOT_EXISTS)))
                    .issueDate(userRequest.getIssueDate())
                    .build();
        }

        if (userRequest.getPassportSerialNumber() == null &&
                userRequest.getCountryCode() == null &&
                userRequest.getIssueDate() == null) {
            return null;
        }

        throw new NotEnoughInformationException(NOT_ENOUGH_INFORMATION);
    }
}