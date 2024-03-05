package com.robot.hotel.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for create and update user")
public class UserRequest {
    @NotBlank(message = "Firstname is required")
    @Size(max = 50, message = "Max size for firstname is 50 characters")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    @Size(max = 50, message = "Max size for lastname is 50 characters")
    private String lastName;

    @NotBlank(message = "Tel.number is required")
    @Pattern(regexp = "^(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}$", message = "Not valid tel.number")
    @Size(max = 20, message = "Max size for tel.number is 20 characters")
    private String telNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Not valid email")
    @Size(max = 50, message = "Max size for email is 50 characters")
    private String email;

    @Size(max = 50, message = "Max size for passport serial number is 50 characters")
    private String passportSerialNumber;
}
