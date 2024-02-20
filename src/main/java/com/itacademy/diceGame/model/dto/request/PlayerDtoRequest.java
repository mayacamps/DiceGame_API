package com.itacademy.diceGame.model.dto.request;

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
    @Size(min=1, max = 15, message = "Introduce a name. Cannot be more than 15 characters long.")
    private String name;
}
