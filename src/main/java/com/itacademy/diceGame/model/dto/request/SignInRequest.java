package com.itacademy.diceGame.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    @NotBlank(message = "Please enter a valid email.")
    @Email(message = "Please enter a valid email. Example: example@example.com",
            regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;
    @NotBlank(message = "Please enter a password.")
    private String password;
}
