package com.itacademy.diceGame.repository;

import com.itacademy.diceGame.model.entity.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamesRepository extends MongoRepository<Game, String> {
    List<Game> findByPlayerId(String id);
}
