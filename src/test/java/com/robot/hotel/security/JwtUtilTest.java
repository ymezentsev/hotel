package com.robot.hotel.security;

import com.robot.hotel.ContainerConfiguration;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ContainerConfiguration.class)
class JwtUtilTest {
    @Autowired
    JwtUtil jwtUtil;

    @Test
    @DisplayName("Generate JWT token")
    void generateTokenTest() {
        assertNotNull(jwtUtil.generateToken("test"));
    }

    @Test
    @DisplayName("Successfully validate JWT token")
    void isValidTokenTest() {
        String jwtToken = jwtUtil.generateToken("test");
        assertTrue(jwtUtil.isValidToken(jwtToken));
    }

    @Test
    @DisplayName("Failed validate JWT token")
    void isValidTokenThrowJwtExceptionTest() {
        assertThrows(JwtException.class, () -> jwtUtil.isValidToken("wrongToken"));
    }

    @Test
    @DisplayName("Get username from JWT token")
    void getUsernameTest() {
        String jwtToken = jwtUtil.generateToken("test");
        assertEquals("test", jwtUtil.getUsernameFromJwtToken(jwtToken));
    }
}