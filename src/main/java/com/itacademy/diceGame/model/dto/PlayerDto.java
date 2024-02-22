package com.itacademy.diceGame.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;

import java.text.DecimalFormat;
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
        DecimalFormat df = new DecimalFormat("####0.00");
        return (successRate == null) ? null : Double.valueOf(df.format(successRate));
    }
}
