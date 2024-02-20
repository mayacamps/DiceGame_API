package com.itacademy.diceGame.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Game{
    private int dice1;
    private int dice2;

    public Game(int dice1, int dice2){
        this.dice1 = dice1;
        this.dice2 = dice2;
    }

}
