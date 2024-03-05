package com.itacademy.diceGame.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Please enter a first name.")
    private String firstName;
    @NotBlank(message = "Please enter a last name.")
    private String lastName;
    @NotNull(message = "Please enter an email.")
    @Email(message = "Please enter a valid email. Example: example@example.com",
            regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;
    @NotBlank(message = "Please enter a password.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
