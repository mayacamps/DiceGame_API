package com.itacademy.diceGame.service.impl;

import com.itacademy.diceGame.repository.*;
import com.itacademy.diceGame.service.GamesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class GamesServiceImpl implements GamesService {
    private PlayerRepository playerRepository;
    private GamesRepository gamesRepository;

}
