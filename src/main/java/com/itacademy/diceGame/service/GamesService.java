package com.itacademy.diceGame.service;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.entity.Game;

import java.util.List;

public interface GamesService {
    void addGameHistory(Long id);
    List<GameDto> getAllGamesDtoByPlayerId(Long id);
    GameDto playGame(Long id);
    void deleteAllGames(Long id);
    Double getSuccessRate(Long id);
    GameDto gameEntityToDto(Game game);
    Game gameDtoToEntity(GameDto gameDto);
}
