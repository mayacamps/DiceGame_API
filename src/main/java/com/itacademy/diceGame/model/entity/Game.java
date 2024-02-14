package com.itacademy.diceGame.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "games")
@AllArgsConstructor
@NoArgsConstructor
public class Game implements Serializable {
    @Id
    private String id;
    private int dice1;
    private int dice2;
    private Player player;

    public Game(int dice1, int dice2, Player player){
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.player = player;
    }

}
