package com.itacademy.diceGame.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerDtoRequest {
    @NotBlank(message = "Please enter a name.")
    @Size(min = 2, max = 20)
    private String name;
}
