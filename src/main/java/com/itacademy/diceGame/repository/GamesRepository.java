package com.itacademy.diceGame.repository;

import com.itacademy.diceGame.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamesRepository extends JpaRepository<Game, Long> {
    List<Game> findByPlayerId(long id);
    void deleteByPlayerId(long id);
}
