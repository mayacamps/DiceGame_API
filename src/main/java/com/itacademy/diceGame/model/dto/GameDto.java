package com.itacademy.diceGame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameDto {
    private int dice1;
    private int dice2;
    private String result;

    public GameDto(int dice1, int dice2){
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.result = hasWon(dice1, dice2);
    }

    private String hasWon(int dice1, int dice2){
        boolean won = dice1 + dice2 == 7;
        return won ? "HAS WON" : "HAS LOST";
    }
}
