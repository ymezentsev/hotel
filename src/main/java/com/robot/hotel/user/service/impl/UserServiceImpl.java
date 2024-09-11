package com.robot.hotel.user.service.impl;

import com.robot.hotel.country.Country;
import com.robot.hotel.country.CountryService;
import com.robot.hotel.exception.*;
import com.robot.hotel.search_criteria.SpecificationBuilder;
import com.robot.hotel.security.oauth2.CustomOAuth2User;
import com.robot.hotel.user.dto.UpdateUserRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserSearchParametersDto;
import com.robot.hotel.user.dto.password.ChangePasswordRequestDto;
import com.robot.hotel.user.mapper.UserMapper;
import com.robot.hotel.user.model.ForgotPasswordToken;
import com.robot.hotel.user.model.Passport;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.model.enums.EmailSubject;
import com.robot.hotel.user.model.enums.RoleName;
import com.robot.hotel.user.repository.UserRepository;
import com.robot.hotel.user.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.ACCESS_DENIED;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CountryService countryService;
    private final PassportService passportService;
    private final EmailSenderService emailSenderService;
    private final EmailContentBuilderService emailContentBuilderService;

    private final SpecificationBuilder<User> specificationBuilder;
    private final ForgotPasswordTokenService forgotPasswordTokenService;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_IS_ALREADY_EXISTS = "User with such phone number already exists";
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
    public void update(Long userId, UpdateUserRequestDto updateUserRequestDto) {
        log.info("Updating user with id: {}", userId);
        User userToUpdate = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException(USER_IS_NOT_EXISTS)
        );

        checkIfUserHasAuthorityToUpdateOrDeleteUserInfo(userToUpdate);

        Country country = countryService.getCountryFromPhoneCode(updateUserRequestDto.getPhoneCode());

        Optional<User> existingUser = userRepository.findByPhoneNumber(updateUserRequestDto.getPhoneNumber());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            throw new DuplicateObjectException(USER_IS_ALREADY_EXISTS);
        }

        Passport passport = passportService.getPassportFromUserRequest(
                updateUserRequestDto.getPassportSerialNumber(),
                updateUserRequestDto.getCountryCode(),
                updateUserRequestDto.getIssueDate(),
                userId);

        userToUpdate.setFirstName(updateUserRequestDto.getFirstName().toLowerCase());
        userToUpdate.setLastName(updateUserRequestDto.getLastName().toLowerCase());
        userToUpdate.setCountry(country);
        userToUpdate.setPhoneNumber(updateUserRequestDto.getPhoneNumber());

        if ((passport != null && userToUpdate.getPassport() != null &&
                !userToUpdate.getPassport().getSerialNumber().equals(passport.getSerialNumber())) ||
                (passport != null && userToUpdate.getPassport() == null)) {
            userToUpdate.setPassport(passport);
        }
        userRepository.save(userToUpdate);

        log.info(String.format(SUCCESSFUL_ACTION_WITH_USER, "updated"), userId);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting user with id: {}", id);

        User userToDelete = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(USER_IS_NOT_EXISTS)
        );
        checkIfUserHasAuthorityToUpdateOrDeleteUserInfo(userToDelete);

        if (userToDelete.getReservations().isEmpty()) {
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
                emailContentBuilderService.buildEmailContent(user.getFirstName(), token, EmailSubject.FORGOT_PASSWORD),
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

    @Override
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email;
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                email = userDetails.getUsername();
            } else if (authentication.getPrincipal() instanceof CustomOAuth2User customOAuth2User) {
                email = customOAuth2User.getEmail();
            } else {
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

    private void checkIfUserHasAuthorityToUpdateOrDeleteUserInfo(User user) {
        User currentUser = getCurrentAuthenticatedUser();
        if (!user.getEmail().equals(currentUser.getEmail()) &&
                currentUser.getRoles()
                        .stream()
                        .noneMatch(role ->
                                role.getName().equals(RoleName.ADMIN) || role.getName().equals(RoleName.MANAGER)
                        )) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }
}