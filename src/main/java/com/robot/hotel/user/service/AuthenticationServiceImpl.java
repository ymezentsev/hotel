package com.robot.hotel.user.service;

import com.robot.hotel.security.JwtUtil;
import com.robot.hotel.user.dto.login.LoginRequestDto;
import com.robot.hotel.user.dto.login.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    //todo add test
    @Override
    public LoginResponseDto authenticate(LoginRequestDto request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new LoginResponseDto(jwtUtil.generateToken(request.getEmail()));
    }
}