package com.robot.hotel.user;

import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.search_criteria.SpecificationBuilder;
import com.robot.hotel.user.country.Country;
import com.robot.hotel.user.country.CountryService;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserSearchParameters;
import com.robot.hotel.user.passport.Passport;
import com.robot.hotel.user.passport.PassportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CountryService countryService;
    private final PassportService passportService;
    private final SpecificationBuilder<User> specificationBuilder;

    private static final String USER_IS_ALREADY_EXISTS = "User with such %s is already exists";
    private static final String USER_IS_NOT_EXISTS = "Such user is not exists";
    private static final String RESERVATIONS_FOR_THIS_USER_ARE_EXISTS =
            "This user has reservations. At first delete reservations";
    private static final String SUCCESSFUL_ACTION_WITH_USER = "Successful %s user with id: {}";

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public UserDto findById(Long id) {
        return userMapper.toDto(userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
    }

    @Override
    @Transactional
    public Page<UserDto> search(UserSearchParameters params, Pageable pageable) {
        Specification<User> userSpecification = specificationBuilder.build(params);
        return userRepository.findAll(userSpecification, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public void update(Long userId, RegistrationRequestDto registrationRequestDto) {
        log.info("Updating user with id: {}", userId);
        User userToUpdate = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException(USER_IS_NOT_EXISTS)
        );

        Country country = countryService.getCountryFromPhoneCode(registrationRequestDto.getPhoneCode());

        Optional<User> existingUser = userRepository.findByEmail(registrationRequestDto.getEmail().toLowerCase());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "email"));
        }

        existingUser = userRepository.findByPhoneNumber(registrationRequestDto.getPhoneNumber());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "phone number"));
        }

        Passport passport = passportService.getPassportFromUserRequest(registrationRequestDto, userId);

        userToUpdate.setFirstName(registrationRequestDto.getFirstName().toLowerCase());
        userToUpdate.setLastName(registrationRequestDto.getLastName().toLowerCase());
        userToUpdate.setCountry(country);
        userToUpdate.setPhoneNumber(registrationRequestDto.getPhoneNumber());
        userToUpdate.setEmail(registrationRequestDto.getEmail().toLowerCase());
        userToUpdate.setPassword(registrationRequestDto.getPassword());

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
}