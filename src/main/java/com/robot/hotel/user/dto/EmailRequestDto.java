package com.robot.hotel.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Dto for resend email confirmation")
public class EmailRequestDto {

    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Max size for email is 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", message = "Not valid email")
    @Schema(description = "User email", example = "johndoe@gmail.com")
    private String email;
}
