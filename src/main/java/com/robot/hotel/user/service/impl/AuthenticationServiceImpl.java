package com.robot.hotel.user.service.impl;

import com.robot.hotel.security.JwtUtil;
import com.robot.hotel.user.dto.login.LoginRequestDto;
import com.robot.hotel.user.dto.login.LoginResponseDto;
import com.robot.hotel.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDto authenticate(LoginRequestDto request) {
        log.info("Authenticating user with email: {}", request.getEmail());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User {} successfully authenticated", request.getEmail());
        return new LoginResponseDto(jwtUtil.generateToken(request.getEmail()));
    }
}