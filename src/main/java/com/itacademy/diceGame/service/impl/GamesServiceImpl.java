package com.itacademy.diceGame.service.impl;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.entity.Game;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.repository.*;
import com.itacademy.diceGame.service.GamesService;
import com.itacademy.diceGame.utils.RandomDiceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GamesServiceImpl implements GamesService {
    private final GamesRepository gamesRepository;

    @Override
    public List<GameDto> getAllGamesByPlayerId(String id) {
        List<Game> games = getGames(id);
        List<GameDto> gameDtos = new ArrayList<GameDto>();
        games.forEach(game -> gameDtos.add(gameEntityToDto(game)));
        return gameDtos;
    }

    @Override
    public GameDto playGame(Player player) {
        GameDto gameDto = new GameDto(RandomDiceGenerator.throwDice(), RandomDiceGenerator.throwDice());
        gamesRepository.save(gameDtoToEntity(gameDto, player));
        return gameDto;
    }

    @Override
    public void deleteAllGames(Player player) {
        List<Game> games = getGames(player.getId());
        games.forEach(gamesRepository::delete);
    }

    @Override
    public GameDto gameEntityToDto(Game game) {
        return new GameDto(game.getDice1(), game.getDice2());
    }

    @Override
    public Game gameDtoToEntity(GameDto gameDto, Player player) {
        return new Game(gameDto.getDice1(), gameDto.getDice2(), player);
    }

    private List<Game> getGames(String id){
        return gamesRepository.findByPlayerId(id);
    }
}
