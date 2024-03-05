package com.itacademy.diceGame.model.dto.request;

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
public class PlayerDtoRequest {
    @NotNull(message = "Cannot be null.")
    @Size(max = 15, message = "Cannot be more than 15 characters long.")
    private String name;
}
