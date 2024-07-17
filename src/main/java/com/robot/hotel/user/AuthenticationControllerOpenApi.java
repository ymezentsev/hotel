package com.robot.hotel.user;

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
import org.springframework.web.bind.annotation.RequestBody;

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
}
