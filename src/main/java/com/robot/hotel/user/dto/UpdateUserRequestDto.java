package com.robot.hotel.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for update user")
public class UpdateUserRequestDto {
    @NotBlank(message = "Firstname is required")
    @Size(min = 2, max = 20, message = "Firstname's length must be from 2 to 20 characters long")
    @Pattern(regexp = "[A-zА-яҐЄІЇґєії']{2,20}", message = "Firstname should contains only letters")
    @Schema(description = "User first name", example = "John")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    @Size(min = 2, max = 20, message = "Lastname's length must be from 2 to 20 characters long")
    @Pattern(regexp = "[A-zА-яҐЄІЇґєії']{2,20}", message = "Lastname should contains only letters")
    @Schema(description = "User last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Phone code is required")
    @Pattern(regexp = "^(\\+)\\d{1,4}", message = "Not valid phone code, it must starts with '+' and contains only digits")
    @Size(min = 2, max = 5, message = "Phone code's length must be from 2 to 5 characters long")
    @Schema(description = "Phone code", example = "+380")
    private String phoneCode;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{8,12}", message = "Not valid phone number, it must contains only digits")
    @Size(min = 8, max = 12, message = "Phone number's length must be from 8 to 12 characters long")
    @Schema(description = "User phone number", example = "502464646")
    private String phoneNumber;

    @Size(min = 6, max = 12, message = "Passport serial number's length must be from 6 to 20 characters long")
    @Schema(description = "User's passport serial number", example = "BB123456")
    private String passportSerialNumber;

    @Size(min = 3, max = 3, message = "Country code's length must be 3 characters long")
    @Pattern(regexp = "[A-z]{3}", message = "Country code should contains only latin letters")
    @Schema(description = "User's passport country code", example = "UKR")
    private String countryCode;

    @Past(message = "Issue date must be past")
    @Schema(description = "User's passport issue date", example = "2020-05-16")
    private LocalDate issueDate;
}