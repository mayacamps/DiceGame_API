package com.itacademy.diceGame.service;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import com.itacademy.diceGame.model.entity.Player;

import java.util.List;

public interface PlayerService {
    Player getPlayerByID(Long id);
    List<PlayerDto> getAllPlayersWithSuccessRate();
    void createPlayer(PlayerDtoRequest playerDtoRequest);
    void updateNamePlayer(Long id, PlayerDtoRequest playerDtoRequest);
    List<GameDto> getAllGamesByPlayerId(Long id);
    GameDto playGame(Long id);
    void deleteAllGames(Long id);
    double getAvgSuccessRate();
    List<PlayerDto> getWinner();
    List<PlayerDto> getLoser();
    PlayerDto playerEntityToDto(Player player);
    Player playerDtoRequestToEntity(PlayerDtoRequest playerDtoRequest);
}
