package com.itacademy.diceGame.service.impl;

import com.itacademy.diceGame.exceptions.NoGamesSavedException;
import com.itacademy.diceGame.exceptions.PlayerAlreadyExistsException;
import com.itacademy.diceGame.exceptions.PlayerNotFoundException;
import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.repository.PlayerRepository;
import com.itacademy.diceGame.service.GamesService;
import com.itacademy.diceGame.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    public Player getPlayerByID(Long id){
        return playerRepository.findById(id).orElseThrow(()-> new PlayerNotFoundException("Player not found with ID: " + id));
    }

    @Override
    public List<PlayerDto> getAllPlayersWithSuccessRate(){
        List<Player> playerEntityList = playerRepository.findAll();
        List<PlayerDto> playerDtoList = new ArrayList<>();
        playerEntityList.forEach( player -> {
            playerDtoList.add(playerEntityToDto(player));
        });
        return playerDtoList;
    }

    @Override
    public void createPlayer(PlayerDtoRequest playerDtoRequest) {
        Player player = playerDtoRequestToEntity(playerDtoRequest);
        try{
            playerRepository.save(player);
            gamesService.createGameHistory(player.getId());
        } catch(DataIntegrityViolationException ex){
            throw new PlayerAlreadyExistsException("Player already exists with given name: " + player.getName());
        }
    }

    @Override
    public void updateNamePlayer(Long id, PlayerDtoRequest playerDtoRequest) {
        if (id == null) {
            throw new IllegalArgumentException("Player ID cannot be null");
        }
        Player player = getPlayerByID(id);
        if (!playerDtoRequest.getName().equalsIgnoreCase(player.getName())){
            checkNameNotUsed(playerDtoRequest.getName());
        }
        player.setName(playerDtoRequest.getName());
        playerEntityToDto(playerRepository.save(player));
    }

    private void checkNameNotUsed(String name) {
        playerRepository.findByNameIgnoreCase(name)
                .ifPresent(player -> {
                    throw new PlayerAlreadyExistsException("Player already exists with given name: " + player.getName());
                });
    }

    @Override
    public List<GameDto> getAllGamesByPlayerId(Long id){
        getPlayerByID(id);
        return gamesService.getAllGamesDtoByPlayerId(id);
    }

    @Override
    public GameDto playGame(Long id) {
        getPlayerByID(id);
        return gamesService.playGame(id);
    }

    @Override
    public void deleteAllGames(Long id) {
        getPlayerByID(id);
        gamesService.deleteAllGames(id);
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
        return new PlayerDto(player.getName(), gamesService.getSuccessRate(player.getId()));
    }

    @Override
    public Player playerDtoRequestToEntity(PlayerDtoRequest playerDtoRequest){
        return new Player(playerDtoRequest.getName());
    }
}
