package com.itacademy.diceGame.service.impl;

import com.itacademy.diceGame.exceptions.PlayerNotFoundException;
import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.service.GamesService;
import com.itacademy.diceGame.service.PlayerService;
import com.itacademy.diceGame.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<PlayerDto> getAllPlayersWithSuccessRate(){
        List<Player> playerEntityList = playerRepository.findAll();
        List<PlayerDto> playerDtoList = new ArrayList<>();
        playerEntityList.forEach( player -> {
            playerDtoList.add(new PlayerDto(player.getName(), getSuccessRate(player)));
        });
        return playerDtoList;
    }

    @Override
    public GameDto playGame(Long id) {
        Player player = getPlayerByID(id);
        GameDto gameDto = gamesService.playGame(player);
        updateSuccessRate(player, gameDto);
        return gameDto;
    }

    private void updateSuccessRate(Player player, GameDto gameDto) {
        Double successRate = player.getAvgSuccessRate();
        double isGameWon = 0d;
        if (gameDto.hasWon()){
            isGameWon = 1.0d;
        }
        if (successRate == null){
            successRate = isGameWon * 100;
        } else {
            int gamesPlayed = gamesService.getAllGamesByPlayerId(player.getId()).size();
            int gamesWon = (int) (successRate * gamesPlayed) / 100;
            successRate = (gamesWon + isGameWon) / gamesPlayed * 100;
        }
        player.setAvgSuccessRate(successRate);
        playerRepository.save(player);
    }

    private Double getSuccessRate(Player player){
        try {
            return player.getAvgSuccessRate();
        } catch (NullPointerException ex){
            return null;
        }
    }
}
