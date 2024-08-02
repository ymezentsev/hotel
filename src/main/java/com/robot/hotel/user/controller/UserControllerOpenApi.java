package com.robot.hotel.user.controller;

import com.robot.hotel.user.dto.EmailRequestDto;
import com.robot.hotel.user.dto.RegistrationRequestDto;
import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserSearchParametersDto;
import com.robot.hotel.user.dto.password.ChangePasswordRequestDto;
import com.robot.hotel.user.dto.password.ForgotPasswordRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Users Controller", description = "API to work with Users")
public interface UserControllerOpenApi {

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all users")
    })
    Page<UserDto> findAll(Pageable pageable);

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched user by id"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such user is not exists")
    })
    UserDto findById(@PathVariable Long id);

    @Operation(summary = "Get all users filtered by firstname, lastname, phone number, " +
            "email, role, passport serial number, reservation and country",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched users"),
            @ApiResponse(
                    responseCode = "500",
                    description = "No property found for 'User'")
    })
    Page<UserDto> search(UserSearchParametersDto parameters, Pageable pageable);

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated user",
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
                    description = "Such user is not exists"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such phone code is not exists")
    })
    void update(@PathVariable Long id, @Valid @RequestBody RegistrationRequestDto registrationRequestDto);

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted user"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Failed to delete user. " +
                            "This user has reservations. At first delete reservations"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such user is not exists")
    })
    void deleteById(@PathVariable Long id);

    @Operation(summary = "Send forgot password email")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully sent forgot password email",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation = EmailRequestDto.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Failed to send email message"),
    })
    void forgotPassword(@Valid @RequestBody EmailRequestDto emailRequestDto);

    @Operation(summary = "Reset user password and set new one")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password successfully changed"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid password")
    })
    void resetPassword(@RequestBody @Valid ForgotPasswordRequestDto request,
                       @RequestParam("token") String token);

    @Operation(summary = "Change password",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password successfully changed"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid old password"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized")
    })
    void changePassword(@RequestBody ChangePasswordRequestDto request);
}