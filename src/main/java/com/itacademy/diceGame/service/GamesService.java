package com.itacademy.diceGame.service;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.entity.Game;
import com.itacademy.diceGame.model.entity.Player;

import java.util.List;

public interface GamesService {
    public GameDto playGame(Player player);
    public List<GameDto> getAllGamesByPlayerId(Long id);
    public GameDto gameEntityToDto(Game game);
    public Game gameDtoToEntity(GameDto gameDto, Player player);
}
