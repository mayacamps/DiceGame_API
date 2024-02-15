package com.itacademy.diceGame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {
    private int dice1;
    private int dice2;
    private String result;

    public GameDto(int dice1, int dice2){
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.result = hasWon() ? "WIN" : "LOST";
    }

    public boolean hasWon(){
        return this.dice1 + this.dice2 == 7;
    }
}
