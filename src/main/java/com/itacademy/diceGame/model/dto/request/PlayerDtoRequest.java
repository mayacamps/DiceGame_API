package com.itacademy.diceGame.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDtoRequest {
    @NotBlank(message = "Please enter a name.")
    @Size(min = 2, max = 20)
    private String name;
}
