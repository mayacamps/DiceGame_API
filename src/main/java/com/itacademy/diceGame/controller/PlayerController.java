package com.itacademy.diceGame.controller;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.service.GamesService;
import com.itacademy.diceGame.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/players")
public class PlayerController {
    private final PlayerService playerService;
    private final GamesService gamesService;

    @GetMapping("/{id}/games")
    public ResponseEntity<List<GameDto>> getGamesPlayerById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok().body(gamesService.getGamesPlayerByID(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<PlayerDto>> getAllPlayersWithSuccessRate(){
        return ResponseEntity.ok().body(playerService.getAllPlayersWithSuccessRate());
    }
}
