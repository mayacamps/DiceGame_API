package com.itacademy.diceGame.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;

import java.util.Objects;

@Data
@NoArgsConstructor
public class PlayerDtoRequest {
    @NotNull(message = "cannot be null")
    @Size(min = 2, max = 20)
    private String name;
}
