package com.robot.hotel.user;

import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.NotEnoughInformationException;
import com.robot.hotel.search_criteria.SpecificationBuilder;
import com.robot.hotel.user.country.Country;
import com.robot.hotel.user.country.CountryRepository;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserRequest;
import com.robot.hotel.user.dto.UserSearchParameters;
import com.robot.hotel.user.passport.Passport;
import com.robot.hotel.user.passport.PassportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CountryRepository countryRepository;
    private final PassportRepository passportRepository;
    private final SpecificationBuilder<User> specificationBuilder;

    private static final String USER_IS_ALREADY_EXISTS = "User with such %s is already exists";
    private static final String USER_IS_NOT_EXISTS = "Such user is not exists";
    private static final String RESERVATIONS_FOR_THIS_USER_ARE_EXISTS =
            "This user has reservations. At first delete reservations";
    private static final String TEL_CODE_IS_NOT_EXISTS = "Such phone code is not exists";
    private static final String COUNTRY_IS_NOT_EXISTS = "Such country is not exists";
    private static final String NOT_ENOUGH_INFORMATION = "There is not enough information to save your passport";
    private static final String ROLE_IS_NOT_EXISTS = "Such role is not exists";
    private static final String SUCCESSFUL_ACTION_WITH_USER = "Successful %s user with id: {}";

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public UserDto save(UserRequest userRequest) {
        log.info("Saving user with email: {}", userRequest.getEmail().toLowerCase());

        Country country = getCountryFromTelCode(userRequest.getPhoneCode());

        if (userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "phone number"));
        }

        if (userRepository.existsByEmail(userRequest.getEmail().toLowerCase())) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "email"));
        }

        Passport passport = getPassportFromUserRequest(userRequest, null);

        User newUser = User.builder()
                .firstName(userRequest.getFirstName().toLowerCase())
                .lastName(userRequest.getLastName().toLowerCase())
                .country(country)
                .phoneNumber(userRequest.getPhoneNumber())
                .email(userRequest.getEmail().toLowerCase())
                .password(userRequest.getPassword())
                .role(Role.USER)
                .passport(passport)
                .reservations(Collections.emptySet())
                .build();

        User savedUser = userRepository.save(newUser);
        log.info(String.format(SUCCESSFUL_ACTION_WITH_USER, "created"), savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto findById(Long id) {
        return userMapper.toDto(userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
    }

    @Override
    public UserDto findByEmail(String email) {
        return userMapper.toDto(userRepository
                .findByEmail(email.toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
    }

    @Override
    public UserDto findByPhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("+")) {
            return userMapper.toDto(userRepository
                    .findByFullPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
        } else {
            return userMapper.toDto(userRepository
                    .findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
        }
    }

    @Override
    public UserDto findByPassportSerialNumber(String passportSerialNumber) {
        return userMapper.toDto(userRepository
                .findByPassportSerialNumber(passportSerialNumber.toLowerCase().strip())
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
    }

    @Override
    public Page<UserDto> findByLastName(String lastName, Pageable pageable) {
        return userRepository.findByLastName(lastName.toLowerCase().strip(), pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> findUsersByReservation(Long id, Pageable pageable) {
        return userRepository.findByReservationsId(id, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> findUsersByRole(String role, Pageable pageable) {
        role = role.toUpperCase().strip();

        if (!Arrays.stream(Role.values())
                .map(Enum::name)
                .toList()
                .contains(role)) {
            throw new NoSuchElementException(ROLE_IS_NOT_EXISTS);
        }

        return userRepository.findByRole(Role.valueOf(role), pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> search(UserSearchParameters params, Pageable pageable) {
        Specification<User> userSpecification = specificationBuilder.build(params);
        return userRepository.findAll(userSpecification, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public void update(Long userId, UserRequest userRequest) {
        log.info("Updating user with id: {}", userId);
        User userToUpdate = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException(USER_IS_NOT_EXISTS)
        );

        Country country = getCountryFromTelCode(userRequest.getPhoneCode());

        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail().toLowerCase());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "email"));
        }

        existingUser = userRepository.findByPhoneNumber(userRequest.getPhoneNumber());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "phone number"));
        }

        Passport passport = getPassportFromUserRequest(userRequest, userId);

        userToUpdate.setFirstName(userRequest.getFirstName().toLowerCase());
        userToUpdate.setLastName(userRequest.getLastName().toLowerCase());
        userToUpdate.setCountry(country);
        userToUpdate.setPhoneNumber(userRequest.getPhoneNumber());
        userToUpdate.setEmail(userRequest.getEmail().toLowerCase());
        userToUpdate.setPassword(userRequest.getPassword());

        if (passport != null && userToUpdate.getPassport() != null &&
                !userToUpdate.getPassport().getSerialNumber().equals(passport.getSerialNumber())) {
            userToUpdate.setPassport(passport);
        }
        userRepository.save(userToUpdate);

        log.info(String.format(SUCCESSFUL_ACTION_WITH_USER, "updated"), userId);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting user with id: {}", id);
        if (userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS))
                .getReservations()
                .isEmpty()) {
            userRepository.deleteById(id);
            log.info(String.format(SUCCESSFUL_ACTION_WITH_USER, "deleted"), id);
        } else {
            throw new NotEmptyObjectException(RESERVATIONS_FOR_THIS_USER_ARE_EXISTS);
        }
    }

    private Country getCountryFromTelCode(String telCode) {
        List<Country> countries = countryRepository.findByPhoneCode(telCode);
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