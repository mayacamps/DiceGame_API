package com.itacademy.diceGame.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;

import java.util.Objects;

@NoArgsConstructor
@Data
public class PlayerDto {
    private String name;
    private String avgSuccessRate;

    public PlayerDto(String name, Double avgSuccessRate){
        this.name = WordUtils.capitalize(Objects.requireNonNullElse(name, "ANONYMOUS"));
        this.avgSuccessRate = getPrettySuccessRate(avgSuccessRate);
    }

    private String getPrettySuccessRate(Double avgSuccessRate){
        if (avgSuccessRate == null) return "NO GAMES SAVED";
        return String.valueOf(avgSuccessRate + " %");
    }
}
