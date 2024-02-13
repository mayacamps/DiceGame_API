package com.itacademy.diceGame.service;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.entity.Game;
import com.itacademy.diceGame.model.entity.Player;

import java.util.List;

public interface GamesService {
    List<GameDto> getAllGamesByPlayerId(Long id);
    GameDto playGame(Player player);
    void deleteAllGames(Player player);
    GameDto gameEntityToDto(Game game);
    Game gameDtoToEntity(GameDto gameDto, Player player);
}
