package com.itacademy.diceGame.service.impl;

import com.itacademy.diceGame.exceptions.NoGamesSavedException;
import com.itacademy.diceGame.exceptions.PlayerNotFoundException;
import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.repository.PlayerRepository;
import com.itacademy.diceGame.service.GamesService;
import com.itacademy.diceGame.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final GamesService gamesService;

    @Override
    public List<PlayerDto> getAllPlayersWithSuccessRate(){
        List<Player> playerEntityList = playerRepository.findAll();
        List<PlayerDto> playerDtoList = new ArrayList<>();
        playerEntityList.forEach( player -> {
            playerDtoList.add(new PlayerDto(player.getName(), player.getSuccessRate()));
        });
        return playerDtoList;
    }

    @Override
    public PlayerDto createPlayer(PlayerDto playerDto) {
        Player newPlayer = playerRepository.save(new Player(playerDto.getName()));
        return playerEntityToDto(newPlayer);
    }

    @Override
    public PlayerDto updateNamePlayer(String id, PlayerDtoRequest playerDtoRequest) {
        Player player = getPlayerByID(id);
        if (!player.getName().equalsIgnoreCase(playerDtoRequest.getName())){
            player.setName(playerDtoRequest.getName());
        }
        return playerEntityToDto(playerRepository.save(player));
    }

    @Override
    public List<GameDto> getAllGamesByPlayerId(String id){
        getPlayerByID(id);
        List<GameDto> gameDtoList = gamesService.getAllGamesByPlayerId(id);
        if (gameDtoList.isEmpty()) throw new NoGamesSavedException("No games saved for player with id: " + id);
        return gameDtoList;
    }

    @Override
    public GameDto playGame(String id) {
        Player player = getPlayerByID(id);
        GameDto gameDto = gamesService.playGame(player);
        updateSuccessRate(player, gameDto);
        return gameDto;
    }

    @Override
    public void deleteAllGames(String id) {
        Player player = getPlayerByID(id);
        gamesService.deleteAllGames(player);
        player.setSuccessRate(null);
        playerRepository.save(player);
    }

    @Override
    public double getAvgSuccessRate() {
        List<PlayerDto> playerDtoList = getAllPlayersWithSuccessRate();
        return playerDtoList.stream()
                .filter(playerDto -> playerDto.getSuccessRate() != null)
                .mapToDouble(PlayerDto::getSuccessRate).average()
                .orElseThrow(() -> new NoGamesSavedException("There are no games saved."));
    }

    @Override
    public List<PlayerDto> getWinner() {
        List<PlayerDto> playerDtoList = getAllPlayersWithSuccessRate();
        PlayerDto highestScore = playerDtoList.stream()
                .filter(playerDto -> playerDto.getSuccessRate() != null)
                .max(Comparator.comparing(PlayerDto::getSuccessRate))
                .orElseThrow(() -> new NoGamesSavedException("There are no games saved."));

        return playerDtoList.stream()
                .filter(playerDto -> Objects.equals(playerDto.getSuccessRate(), highestScore.getSuccessRate()))
                .toList();
    }

    @Override
    public List<PlayerDto> getLoser() {
        List<PlayerDto> playerDtoList = getAllPlayersWithSuccessRate();
        PlayerDto lowestScore = playerDtoList.stream()
                .filter(playerDto -> playerDto.getSuccessRate() != null)
                .min(Comparator.comparing(PlayerDto::getSuccessRate))
                .orElseThrow(() -> new NoGamesSavedException("There are no games saved."));

        return playerDtoList.stream()
                .filter(playerDto -> Objects.equals(playerDto.getSuccessRate(), lowestScore.getSuccessRate()))
                .toList();
    }

    @Override
    public PlayerDto playerEntityToDto(Player player) {
        return new PlayerDto(player.getName(), player.getSuccessRate());
    }

    @Override
    public Player playerDtoRequestToEntity(PlayerDtoRequest playerDtoRequest){
        return new Player(playerDtoRequest.getName());
    }

    private Player getPlayerByID(String id){
        return playerRepository.findById(id).orElseThrow(()-> new PlayerNotFoundException("Player not found with ID: " + id));
    }

    private void updateSuccessRate(Player player, GameDto gameDto) {
        Double successRate = player.getSuccessRate();
        double isGameWon = 0d;
        if (gameDto.hasWon()){
            isGameWon = 1.0d;
        }
        if (successRate == null){
            successRate = isGameWon * 100;
        } else {
            int gamesPlayed = gamesService.getAllGamesByPlayerId(player.getId()).size();
            int gamesWon = (int) Math.ceil((successRate / 100) * (gamesPlayed - 1));
            successRate = (gamesWon + isGameWon) / gamesPlayed * 100;
        }
        player.setSuccessRate(successRate);
        playerRepository.save(player);
    }
}
