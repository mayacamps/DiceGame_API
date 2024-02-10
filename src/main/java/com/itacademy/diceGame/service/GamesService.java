package com.itacademy.diceGame.service;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.entity.Game;

import java.util.List;

public interface GamesService {
    public List<GameDto> getAllGamesByPlayerId(Long id);
    public GameDto gameEntityToDto(Game game);
}
