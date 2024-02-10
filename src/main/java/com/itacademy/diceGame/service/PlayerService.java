package com.itacademy.diceGame.service;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;

import java.util.List;

public interface PlayerService {
    public GameDto playGame(Long id);
    public List<GameDto> getAllGamesByPlayerId(Long id);
    public List<PlayerDto> getAllPlayersWithSuccessRate();
    public double getAvgSuccessRate();
}
