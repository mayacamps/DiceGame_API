package com.itacademy.diceGame.service;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import com.itacademy.diceGame.model.entity.Player;

import java.util.List;

public interface PlayerService {
    Player getPlayerByID(String id);
    List<PlayerDto> getAllPlayersWithSuccessRate();
    PlayerDto createPlayer(PlayerDtoRequest playerDtoRequest);
    PlayerDto updateNamePlayer(String id, PlayerDtoRequest playerDtoRequest);
    List<GameDto> getAllGamesByPlayerId(String id);
    GameDto playGame(String id);
    void deleteAllGames(String id);
    double getAvgSuccessRate();
    List<PlayerDto> getWinner();
    List<PlayerDto> getLoser();
    PlayerDto playerEntityToDto(Player player);
    Player playerDtoRequestToEntity(PlayerDtoRequest playerDtoRequest);
}
