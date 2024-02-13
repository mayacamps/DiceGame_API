package com.itacademy.diceGame.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@Data
public class PlayerDto {
    @Size(min = 2, max = 20)
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
