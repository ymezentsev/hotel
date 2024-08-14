package com.robot.hotel.security;

import com.robot.hotel.ContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class CustomUserDetailsServiceTest {
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("Successfully load user by email")
    void loadUserByUsernameTest() {
        assertNotNull(customUserDetailsService.loadUserByUsername("admin@gmail.com"));
    }

    @Test
    @DisplayName("Failed load user by email")
    void loadUserByUsernameThrowExceptionTest() {
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("test"));
    }
}