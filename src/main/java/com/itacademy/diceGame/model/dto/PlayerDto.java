package com.itacademy.diceGame.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
public class PlayerDto {
    private String name;
    private Double successRate;

    public PlayerDto(String name, Double successRate){
        this.name = WordUtils.capitalize(Objects.requireNonNullElse(name, "ANONYMOUS"));
        this.successRate = formatDouble(successRate);
    }

    private Double formatDouble(Double successRate){
        return (successRate == null) ? null : new BigDecimal(successRate).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
