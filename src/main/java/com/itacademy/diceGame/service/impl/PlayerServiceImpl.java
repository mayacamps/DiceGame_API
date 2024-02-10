package com.itacademy.diceGame.service.impl;

import com.itacademy.diceGame.exceptions.PlayerNotFoundException;
import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.service.GamesService;
import com.itacademy.diceGame.service.PlayerService;
import com.itacademy.diceGame.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository playerRepository;
    private GamesService gamesService;

    private Player getPlayerByID(Long id){
        return playerRepository.findById(id).orElseThrow(()-> new PlayerNotFoundException("Player not found with ID: " + id));
    }

    public List<GameDto> getAllGamesByPlayerId(Long id){
        getPlayerByID(id);
        return gamesService.getAllGamesByPlayerId(id);
    }
}
