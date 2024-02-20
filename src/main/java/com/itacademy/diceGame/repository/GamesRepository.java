package com.itacademy.diceGame.repository;

import com.itacademy.diceGame.model.entity.GameHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesRepository extends MongoRepository<GameHistory, String> {
    GameHistory findByPlayerId(Long id);
}
