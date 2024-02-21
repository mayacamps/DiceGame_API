package com.itacademy.diceGame.service.impl;

import com.itacademy.diceGame.exceptions.NoGamesSavedException;
import com.itacademy.diceGame.exceptions.PlayerNotFoundException;
import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.entity.Game;
import com.itacademy.diceGame.model.entity.GameHistory;
import com.itacademy.diceGame.repository.GamesRepository;
import com.itacademy.diceGame.service.GamesService;
import com.itacademy.diceGame.utils.RandomDiceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GamesServiceImpl implements GamesService {
    private final GamesRepository gamesRepository;

    @Override
    public void createGameHistory(Long id) {
        gamesRepository.save(new GameHistory(id));
    }

    @Override
    public List<GameDto> getAllGamesDtoByPlayerId(Long id) {
        List<Game> games = getAllGamesByPlayerId(id);
        List<GameDto> gameDtos = new ArrayList<>();
        games.forEach(game -> gameDtos.add(gameEntityToDto(game)));
        return gameDtos;
    }

    private List<Game> getAllGamesByPlayerId(Long id) {
        GameHistory gameHistory = getGameHistoryById(id);
        List<Game> games = gameHistory.getGames();
        if (games.isEmpty()) throw new NoGamesSavedException("No games saved for player with id: " + id);
        return games;
    }

    private GameHistory getGameHistoryById(Long id){
        try{
            return gamesRepository.findByPlayerId(id);
        } catch (NoSuchElementException ex){
            throw new PlayerNotFoundException("Player not found with id: " + id);
        }
    }

    @Override
    public GameDto playGame(Long id) {
        GameDto gameDto = new GameDto(RandomDiceGenerator.throwDice(), RandomDiceGenerator.throwDice());
        updateGameHistory(id, gameDto);
        return gameDto;
    }

    private void updateGameHistory(Long id, GameDto gameDto){
        GameHistory gameHistory = getGameHistoryById(id);
        Double updatedSuccessRate = updateSuccessRate(gameHistory, gameDto);

        gameHistory.addGame(gameDtoToEntity(gameDto));
        gameHistory.setSuccessRate(updatedSuccessRate);
        gamesRepository.save(gameHistory);
    }

    private Double updateSuccessRate(GameHistory gameHistory, GameDto gameDto) {
        Double successRate = gameHistory.getSuccessRate();
        double isGameWon = 0d;
        if (gameDto.hasWon()){
            isGameWon = 1.0d;
        }
        if (successRate == null){
            successRate = isGameWon * 100;
        } else {
            int gamesPlayed = gameHistory.getGames().size();
            int gamesWon = (int) Math.ceil((successRate / 100) * gamesPlayed);
            successRate = (gamesWon + isGameWon) / (gamesPlayed + 1) * 100;
        }
        return successRate;
    }

    @Override
    public void deleteAllGames(Long id) {
        GameHistory gameHistory = getGameHistoryById(id);
        gameHistory.setSuccessRate(null);
        if (gameHistory.getGames().isEmpty()) throw new NoGamesSavedException("No games saved for player with id: " + id);
        gameHistory.setGames(new ArrayList<>());
        gamesRepository.save(gameHistory);
    }

    @Override
    public Double getSuccessRate(Long id) {
        GameHistory gameHistory = getGameHistoryById(id);
        return gameHistory.getSuccessRate();
    }

    @Override
    public GameDto gameEntityToDto(Game game) {
        return new GameDto(game.getDice1(), game.getDice2());
    }

    @Override
    public Game gameDtoToEntity(GameDto gameDto) {
        return new Game(gameDto.getDice1(), gameDto.getDice2());
    }
}
