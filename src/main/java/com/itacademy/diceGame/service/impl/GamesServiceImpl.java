package com.itacademy.diceGame.service.impl;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.entity.Game;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.repository.*;
import com.itacademy.diceGame.service.GamesService;
import com.itacademy.diceGame.utils.RandomDiceGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class GamesServiceImpl implements GamesService {
    private GamesRepository gamesRepository;

    @Override
    public GameDto playGame(Player player) {
        GameDto gameDto = new GameDto(RandomDiceGenerator.throwDice(), RandomDiceGenerator.throwDice());
        gamesRepository.save(gameDtoToEntity(gameDto, player));
        return gameDto;
    }

    @Override
    public List<GameDto> getAllGamesByPlayerId(Long id) {
        List<Game> games = gamesRepository.findByPlayerId(id);
        List<GameDto> gameDtos = new ArrayList<GameDto>();
        games.forEach(game -> gameDtos.add(gameEntityToDto(game)));
        return gameDtos;
    }

    @Override
    public GameDto gameEntityToDto(Game game) {
        return new GameDto(game.getDice1(), game.getDice2());
    }

    @Override
    public Game gameDtoToEntity(GameDto gameDto, Player player) {
        return new Game(gameDto.getDice1(), gameDto.getDice2(), player);
    }
}
