package com.robot.hotel.user.service;

import com.robot.hotel.country.Country;
import com.robot.hotel.country.CountryService;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.InvalidPasswordException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.UserNotAuthenticatedException;
import com.robot.hotel.search_criteria.SpecificationBuilder;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserSearchParametersDto;
import com.robot.hotel.user.dto.password.ChangePasswordRequestDto;
import com.robot.hotel.user.mapper.UserMapper;
import com.robot.hotel.user.model.EmailSubject;
import com.robot.hotel.user.model.ForgotPasswordToken;
import com.robot.hotel.user.model.Passport;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final ForgotPasswordTokenService forgotPasswordTokenService;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_IS_ALREADY_EXISTS = "User with such %s already exists";
    private static final String USER_IS_NOT_EXISTS = "Such user not exists";
    private static final String RESERVATIONS_FOR_THIS_USER_ARE_EXISTS =
            "This user has reservations. At first delete reservations";
    private static final String SUCCESSFUL_ACTION_WITH_USER = "Successful %s user with id: {}";
    private static final String USER_NOT_AUTHENTICATED = "User is not authenticated";
    private static final String INVALID_PASSWORD = "Old password field do not match user password";

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
    public Page<UserDto> search(UserSearchParametersDto params, Pageable pageable) {
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

    @Override
    public void enableUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void sendForgotPasswordEmail(String email) {
        log.info("Sending forgot password email to: {}", email);

        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new NoSuchElementException(USER_IS_NOT_EXISTS));

        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenService.generateForgotPasswordToken(user);
        String token = forgotPasswordTokenService.saveForgotPasswordToken(forgotPasswordToken);

        emailSenderService.send(
                user.getEmail().toLowerCase(),
                emailSenderService.buildEmailContent(user.getFirstName(), token, EmailSubject.FORGOT_PASSWORD),
                EmailSubject.FORGOT_PASSWORD.getSubject());
        log.info("Forgot password email sent successfully to: {}", email);
    }

    @Override
    public void resetPassword(String newPassword, String token) {
        log.info("Changing user's password by token: {}", token);
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenService.getForgotPasswordToken(token);
        forgotPasswordTokenService.validateForgotPasswordToken(forgotPasswordToken);

        forgotPasswordToken.setConfirmedAt(LocalDateTime.now());
        forgotPasswordTokenService.saveForgotPasswordToken(forgotPasswordToken);

        User user = forgotPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("User with email: {} has successful changed password", user.getEmail());
    }

    @Override
    public void changePassword(ChangePasswordRequestDto request) {
        log.info("Changing password method");
        User user = getCurrentAuthenticatedUser();
        log.info("Changing password for user with email: {}", user.getEmail());

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException(INVALID_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("User with email: {} has successful changed password", user.getEmail());
    }

    //todo add tests
    @Override
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email;
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                email = userDetails.getUsername();
            } /*else if (authentication.getPrincipal() instanceof CustomOAuth2User customOAuth2User) {
                email = customOAuth2User.getEmail();
            }*/ else {
                email = null;
            }

            if (email != null) {
                return userRepository
                        .findByEmail(email.toLowerCase())
                        .orElseThrow(() -> new UsernameNotFoundException(USER_IS_NOT_EXISTS));
            }
        }
        throw new UserNotAuthenticatedException(USER_NOT_AUTHENTICATED);
    }
}