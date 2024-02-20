package com.itacademy.diceGame.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@NoArgsConstructor
public class PlayerDto {
    private String name;
    private Double successRate;

    public PlayerDto(String name, Double successRate){
        this.name = name;
        this.successRate = formatDouble(successRate);
    }

    private Double formatDouble(Double successRate){
        return (successRate == null) ? null : new BigDecimal(successRate).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
