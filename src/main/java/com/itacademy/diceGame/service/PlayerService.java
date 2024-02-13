package com.itacademy.diceGame.service;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.entity.Player;

import java.util.List;

public interface PlayerService {
    public List<PlayerDto> getAllPlayersWithSuccessRate();
    public List<GameDto> getAllGamesByPlayerId(Long id);
    public GameDto playGame(Long id);
    public void deleteAllGames(Long id);
    public double getAvgSuccessRate();
    public PlayerDto getWinner();
    public List<PlayerDto> getLoser();
    public PlayerDto playerEntityToDto(Player player);
}
