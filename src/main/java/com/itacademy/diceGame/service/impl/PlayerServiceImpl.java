package com.itacademy.diceGame.service.impl;

import com.itacademy.diceGame.service.PlayerService;
import com.itacademy.diceGame.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository playerRepository;
}
