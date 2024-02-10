package com.itacademy.diceGame.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;

import java.util.Objects;

@NoArgsConstructor
@Data
public class PlayerDto {
    private String name;
    private String successRate;

    public PlayerDto(String name, Double successRate){
        this.name = WordUtils.capitalize(Objects.requireNonNullElse(name, "ANONYMOUS"));
        this.successRate = getPrettySuccessRate(successRate);
    }

    private String getPrettySuccessRate(Double successRate){
        if (successRate == null) return "NO GAMES SAVED";
        return String.valueOf(successRate + " %");
    }
}
