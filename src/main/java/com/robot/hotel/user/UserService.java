package com.robot.hotel.user;

import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.NotEmptyObjectException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String USER_IS_ALREADY_EXISTS = "User with such %s is already exists";
    private static final String USER_IS_NOT_EXISTS = "Such user is not exists";
    private static final String RESERVATIONS_FOR_THIS_USER_ARE_EXISTS =
            "This user has reservations. At first delete reservations";

    @Transactional
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::buildUserDto)
                .toList();
    }

    public UserDto save(UserRequest userRequest) {
        userRequest.setTelNumber(updateTelNumber(userRequest.getTelNumber()));

        if (userRepository.existsByEmail(userRequest.getEmail().toLowerCase().strip())) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "email"));
        }

        if (userRepository.existsByTelNumber(userRequest.getTelNumber())) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "tel.number"));
        }

        if (Objects.nonNull(userRequest.getPassportSerialNumber()) && !userRequest.getPassportSerialNumber().isBlank()
                && userRepository.existsByPassportSerialNumber(
                userRequest.getPassportSerialNumber().toLowerCase().strip())) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "passport"));
        }

        User newUser = userMapper.buildUserFromRequest(userRequest);
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
        return userMapper.buildUserDto(userRepository
                .findByTelNumber(updateTelNumber(telNumber))
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS)));
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

    public void update(Long id, UserRequest userRequest) {
        User userToUpdate = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(USER_IS_NOT_EXISTS)
        );

        userRequest.setTelNumber(updateTelNumber(userRequest.getTelNumber()));

        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail().toLowerCase().strip());
        if (existingUser.isPresent() && !Objects.equals(existingUser.get().getId(), id)) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "email"));
        }

        existingUser = userRepository.findByTelNumber(userRequest.getTelNumber());
        if (existingUser.isPresent() && !Objects.equals(existingUser.get().getId(), id)) {
            throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "tel.number"));
        }

        if (Objects.nonNull(userRequest.getPassportSerialNumber()) && !userRequest.getPassportSerialNumber().isBlank()) {
            existingUser = userRepository.findByPassportSerialNumber(
                    userRequest.getPassportSerialNumber().toLowerCase().strip());
            if (existingUser.isPresent() && !Objects.equals(existingUser.get().getId(), id)) {
                throw new DuplicateObjectException(String.format(USER_IS_ALREADY_EXISTS, "passport"));
            }
        } else {
            userRequest.setPassportSerialNumber("");
        }

        userToUpdate.setFirstName(userRequest.getFirstName().strip());
        userToUpdate.setLastName(userRequest.getLastName().strip());
        userToUpdate.setTelNumber(userRequest.getTelNumber().strip());
        userToUpdate.setEmail(userRequest.getEmail().strip());
       // userToUpdate.setPassportSerialNumber(userRequest.getPassportSerialNumber().strip());
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

    private String updateTelNumber(String telNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = telNumber.toCharArray();
        if (chars[0] == '+') {
            stringBuilder.append(chars[0]);
        }

        for (char ch : chars) {
            if (Character.isDigit(ch)) {
                stringBuilder.append(ch);
            }
        }
        return stringBuilder.toString();
    }
}
