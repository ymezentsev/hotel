package com.robot.hotel.user.dto.password;

import com.robot.hotel.user.validation.FieldMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch.List({
        @FieldMatch(first = "newPassword",
                second = "repeatNewPassword",
                message = "Passwords do not match")
})
public class ChangePasswordRequestDto {

    @NotBlank(message = "Current password is required")
    @Schema(description = "User Current Password", example = "Qwerty123456")
    private String oldPassword;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "The password size must be from 8 to 100 characters long")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,100}$",
            message = "Password should have 8 or more characters and contains digits, " +
                    "letters in upper case and letters in lower case")
    @Schema(description = "User new password", example = "Qwerty123456")
    private String newPassword;


    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "The password size must be from 8 to 100 characters long")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,100}$",
            message = "Password should have 8 or more characters and contains digits, " +
                    "letters in upper case and letters in lower case")
    @Schema(description = "User new password", example = "Qwerty123456")
    private String repeatNewPassword;
}
