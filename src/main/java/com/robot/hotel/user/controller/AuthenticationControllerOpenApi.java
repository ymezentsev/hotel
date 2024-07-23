package com.robot.hotel.user.controller;

import com.robot.hotel.user.dto.EmailRequestDto;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Authentication Controller", description = "API to work with authentication")
public interface AuthenticationControllerOpenApi {

    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation = RegistrationRequestDto.class))
                    }),
            @ApiResponse(
                    responseCode = "409",
                    description = "Such user is already exists"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such phone code is not exists"),
            @ApiResponse(
                    responseCode = "400",
                    description = "There is not enough information to save user's passport")
    })
    UserDto register(@Valid @RequestBody RegistrationRequestDto registrationRequestDto);

    @Operation(summary = "Email Verification")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email successfully confirmed"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email is already verified"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token has expired")
    })
    ResponseEntity<Void> confirm(@RequestParam("token") String token);

    @Operation(summary = "Resend message for email verification")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email sent successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Failed to send email message"),
    })
    void resendConfirmationEmail(@RequestBody EmailRequestDto request);
}
