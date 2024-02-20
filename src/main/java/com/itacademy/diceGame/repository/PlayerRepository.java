package com.itacademy.diceGame.repository;

import com.itacademy.diceGame.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByNameIgnoreCase(String name);
}
