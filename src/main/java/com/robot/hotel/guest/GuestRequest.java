package com.robot.hotel.guest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for create and update guest")
public class GuestRequest {
    @NotBlank(message = "Firstname is required")
    @Size(max = 50, message = "Max size for firstname is 50 characters")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    @Size(max = 50, message = "Max size for lastname is 50 characters")
    private String lastName;

    @NotBlank(message = "Tel.number is required")
    @Size(min = 10, max = 20, message = "Max size for tel.number is 20 characters, min size - 10 characters")
    private String telNumber;

    @Email(message = "Email must be valid")
    @Size(max = 50, message = "Max size for email is 50 characters")
    private String email;

    @Size(max = 50, message = "Max size for passport serial number is 50 characters")
    private String passportSerialNumber;
}
