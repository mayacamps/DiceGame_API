package com.itacademy.diceGame.repository;

import com.itacademy.diceGame.model.entity.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
    Optional<Player> findByNameIgnoreCase(String name);
}
