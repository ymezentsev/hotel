package com.robot.hotel.user;

import com.robot.hotel.user.dto.UserDto;
import com.robot.hotel.user.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Users Controller", description = "API to work with Users")
public interface UserControllerOpenApi {

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all users")
    })
    List<UserDto> findAll();

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully created",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation = UserRequest.class))
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
    UserDto save(@Valid @RequestBody UserRequest userRequest);

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

    @Operation(summary = "Get user by email")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched user by email"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such user is not exists")
    })
    UserDto findByEmail(@PathVariable String email);

    @Operation(summary = "Get user by phone number")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched user by phone number"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such user is not exists")
    })
    UserDto findByPhoneNumber(@PathVariable String phoneNumber);

    @Operation(summary = "Get user by passport number")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched user by passport number"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such user is not exists")
    })
    UserDto findByPassportSerialNumber(@PathVariable String passportSerialNumber);

    @Operation(summary = "Get users by lastname")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched users by lastname")
    })
    List<UserDto> findByLastName(@PathVariable String lastName);

    @Operation(summary = "Get users by reservation")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched users by reservation")
    })
    List<UserDto> findUsersByReservation(@PathVariable Long id);

    @Operation(summary = "Get users by role")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched users by role"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Such role is not exists")
    })
    List<UserDto> findUsersByRole(@PathVariable String role);

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated user",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema =
                                    @Schema(implementation = UserRequest.class))
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
    void update(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest);

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
}