package com.robot.hotel.user.service.impl;

import com.robot.hotel.ContainerConfiguration;
import com.robot.hotel.DBInitializer;
import com.robot.hotel.user.dto.login.LoginRequestDto;
import com.robot.hotel.user.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class AuthenticationServiceImplTest {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    DBInitializer dbInitializer;

    @BeforeEach
    void setUp() {
        dbInitializer.populateDB();
    }

    @Test
    @DisplayName("Successful user authentication")
    void authenticateTest() {
        authenticationService.authenticate(new LoginRequestDto("admin@gmail.com", "Admin123"));

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertThat(principal.getUsername()).isEqualTo("admin@gmail.com");
    }

    @Test
    @DisplayName("Fail user authentication")
    void authenticateThrowExceptionTest() {
        assertThrows(BadCredentialsException.class,
                () -> authenticationService.authenticate(
                        new LoginRequestDto("sidor_andr@gmail.com", "wrongPassword")));
    }
}