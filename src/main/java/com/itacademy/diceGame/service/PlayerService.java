package com.itacademy.diceGame.service;

import com.itacademy.diceGame.model.dto.PlayerDto;

import java.util.List;

public interface PlayerService {
    public List<PlayerDto> getAllPlayersWithSuccessRate();
}
