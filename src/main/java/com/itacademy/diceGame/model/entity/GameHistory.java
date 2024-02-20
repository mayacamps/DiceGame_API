package com.itacademy.diceGame.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "game_histories")
public class GameHistory {
    @Id
    private String id;
    @Indexed(unique = true)
    private Long playerId;
    private List<Game> games;
    private Double successRate;

    public GameHistory(Long playerId){
        this.playerId = playerId;
        games = new ArrayList<>();
        successRate = null;
    }

    public void addGame(Game game){
        games.add(game);
    }
}
