package com.robot.hotel.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for create and update user")
public class UserRequest {
    @NotBlank(message = "Firstname is required")
    @Size(min = 2, max = 20, message = "Firstname's length must be from 2 to 20 characters long")
    @Pattern(regexp = "[A-zА-яҐЄІЇґєії']{2,20}", message = "Firstname should contains only letters")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    @Size(min = 2, max = 20, message = "Lastname's length must be from 2 to 20 characters long")
    @Pattern(regexp = "[A-zА-яҐЄІЇґєії']{2,20}", message = "Lastname should contains only letters")
    private String lastName;

    @NotBlank(message = "Phone code is required")
    @Pattern(regexp = "^(\\+)\\d{1,4}", message = "Not valid phone code, it must starts with '+' and contains only digits")
    @Size(min = 2, max = 5, message = "Phone code's length must be from 2 to 5 characters long")
    private String phoneCode;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{8,12}", message = "Not valid phone number, it must contains only digits")
    @Size(min = 8, max = 12, message = "Phone number's length must be from 8 to 12 characters long")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Not valid email")
    @Size(max = 50, message = "Max size for email is 50 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,100}$",
            message = "Password should have 8 or more characters and contains digits, " +
                    "letters in upper case and letters in lower case")
    private String password;

    @Size(min = 6, max = 12, message = "Passport serial number's length must be from 6 to 20 characters long")
    private String passportSerialNumber;

    @Size(min = 3, max = 3, message = "Country code's length must be 3 characters long")
    @Pattern(regexp = "[A-z]{3}", message = "Country code should contains only latin letters")
    private String countryCode;

    @Past(message = "Issue date must be past")
    private LocalDate issueDate;
}