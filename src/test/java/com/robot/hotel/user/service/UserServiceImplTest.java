package com.robot.hotel.user.service;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.DBAuthentication;
import com.robot.hotel.TestDBUtils;
import com.robot.hotel.exception.DuplicateObjectException;
import com.robot.hotel.exception.InvalidPasswordException;
import com.robot.hotel.exception.NotEmptyObjectException;
import com.robot.hotel.exception.UserNotAuthenticatedException;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserSearchParametersDto;
import com.robot.hotel.user.dto.password.ChangePasswordRequestDto;
import com.robot.hotel.user.model.User;
import com.robot.hotel.user.repository.ForgotPasswordTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class UserServiceImplTest {
    @Autowired
    UserService userService;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    TestDBUtils testDBUtils;

    @Autowired
    ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DBAuthentication DBAuthentication;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Find all users")
    void findAllTest() {
        assertEquals(6, userService.findAll(Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Successful find user by id")
    void findByIdTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        assertEquals("+380965467834", userService.findById(id).phoneNumber());
    }

    @Test
    @DisplayName("Fail find user by id")
    void findByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findById(200L));
    }

    @Test
    @DisplayName("Successful search users")
    void searchTest() {
        UserSearchParametersDto searchFirstname =
                new UserSearchParametersDto(new String[]{"andriy"},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{});
        UserSearchParametersDto searchLastname =
                new UserSearchParametersDto(new String[]{},
                        new String[]{"sidor"},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{});
        UserSearchParametersDto searchPhoneNumber =
                new UserSearchParametersDto(new String[]{},
                        new String[]{},
                        new String[]{"+380"},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{});
        UserSearchParametersDto searchRole =
                new UserSearchParametersDto(new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{"USer"},
                        new String[]{},
                        new String[]{},
                        new String[]{});
        UserSearchParametersDto searchReservation =
                new UserSearchParametersDto(new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{testDBUtils.getReservationIdByRoom("101").toString(),
                                testDBUtils.getReservationIdByRoom("203").toString()},
                        new String[]{});
        UserSearchParametersDto searchCountry =
                new UserSearchParametersDto(new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{},
                        new String[]{"UKR"});

        assertAll(
                () -> assertEquals(2, userService.search(searchFirstname, Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(2, userService.search(searchLastname, Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(5, userService.search(searchPhoneNumber, Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(4, userService.search(searchRole, Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(4, userService.search(searchReservation, Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(2, userService.search(searchCountry, Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Successful update user")
    void updateTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        userService.update(id, registrationRequestDto);
        assertAll(
                () -> assertEquals("dmitro", userService.findById(id).firstName()),
                () -> assertEquals(6, userService.findAll(Pageable.unpaged()).getTotalElements())
        );
    }

    @Test
    @DisplayName("Fail update user (throw NoSuchElementException - wrong user id)")
    void updateThrowNoSuchElementExceptionWrongUserIdTest() {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "semenov@gmail.com", "Password1", "Password1",
                "df123456", "usa", LocalDate.of(2018, 3, 8));

        assertThrows(NoSuchElementException.class,
                () -> userService.update(100L, registrationRequestDto));
    }

    @Test
    @DisplayName("Fail update user (throw DuplicateObjectException - wrong email)")
    void updateThrowDuplicateObjectExceptionWrongEmailTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "0953453434", "kozlov@gmail.com", "Password1", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        assertThrows(DuplicateObjectException.class,
                () -> userService.update(id, registrationRequestDto));
    }

    @Test
    @DisplayName("Fail update user (throw DuplicateObjectException - wrong phone number)")
    void updateThrowDuplicateObjectExceptionWrongPhoneTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto("dmitro", "semenov", "+1",
                "505463213", "semenov@gmail.com", "Password1", "Password1",
                "df123456", "UKR", LocalDate.of(2018, 3, 8));

        assertThrows(DuplicateObjectException.class,
                () -> userService.update(id, registrationRequestDto));
    }

    @Test
    @DisplayName("Successful delete user")
    void deleteByIdTest() {
        Long id = testDBUtils.getUserIdByEmail("dmitr@gmail.com");
        userService.deleteById(id);
        assertEquals(5, userService.findAll(Pageable.unpaged()).getTotalElements());
    }

    @Test
    @DisplayName("Fail delete user (throw NoSuchElementException)")
    void deleteByIdThrowNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.deleteById(1000L));
    }

    @Test
    @DisplayName("Fail delete user (throw NotEmptyObjectException)")
    void deleteByIdThrowNotEmptyObjectExceptionTest() {
        Long id = testDBUtils.getUserIdByEmail("sidor@gmail.com");

        assertThrows(NotEmptyObjectException.class,
                () -> userService.deleteById(id));
    }

    @Test
    @DisplayName("Enable user")
    void enableUser() {
        User user = testDBUtils.getUserByEmail("nikola@gmail.com");
        assertAll(
                () -> assertFalse(user.isEnabled()),
                () -> {
                    userService.enableUser(user);
                    assertTrue(user.isEnabled());
                }
        );
    }

    @Test
    @DisplayName("Successful send forgot password email")
    void sendForgotPasswordEmailTest() {
        userService.sendForgotPasswordEmail("sidor@gmail.com");
        assertEquals(4, forgotPasswordTokenRepository.findAll().size());
    }

    @Test
    @DisplayName("Failed send confirmation email (throws NoSuchElementException)")
    void sendForgotPasswordEmailThrowsNoSuchElementExceptionTest() {
        assertThrows(NoSuchElementException.class,
                () -> userService.sendForgotPasswordEmail("newTest@mail"));
    }

    @Test
    @DisplayName("Successful replace forgotten password")
    void resetPasswordTest() {
        userService.resetPassword("newPassword", "8ac319b4-990f-466f-8a5a-7c2a028b430c");

        assertTrue(passwordEncoder.matches("newPassword",
                testDBUtils.getUserByEmail("sidor@gmail.com").getPassword()));
    }

    @Test
    @DisplayName("Successful change password")
    void changePasswordTest() {
        DBAuthentication.loginUser();
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto();
        changePasswordRequestDto.setOldPassword("Qwerty123456");
        changePasswordRequestDto.setNewPassword("newPassword");
        changePasswordRequestDto.setRepeatNewPassword("newPassword");

        userService.changePassword(changePasswordRequestDto);
        assertTrue(passwordEncoder.matches("newPassword",
                testDBUtils.getUserByEmail("sidor_andr@gmail.com").getPassword()));
    }

    @Test
    @DisplayName("Failed change password (throws InvalidPasswordException)")
    void changePasswordThrowsInvalidPasswordExceptionTest() {
        DBAuthentication.loginUser();
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto();
        changePasswordRequestDto.setOldPassword("Qwerty");
        changePasswordRequestDto.setNewPassword("newPassword");
        changePasswordRequestDto.setRepeatNewPassword("newPassword");

        assertThrows(InvalidPasswordException.class, () -> userService.changePassword(changePasswordRequestDto));
    }

    @Test
    @DisplayName("Successful get current authenticated user")
    void getCurrentAuthenticatedUserTest() {
        DBAuthentication.loginUser();
        assertEquals("sidor_andr@gmail.com", userService.getCurrentAuthenticatedUser().getEmail());
    }

    @Test
    @DisplayName("Failed get current authenticated user (throws UserNotAuthenticatedException)")
    void getCurrentAuthenticatedUserThrowsUserNotAuthenticatedExceptionTest() {
        assertThrows(UserNotAuthenticatedException.class, () -> userService.getCurrentAuthenticatedUser());
    }
}