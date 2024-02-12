package com.itacademy.diceGame.controller;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/players")
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("/")
    public ResponseEntity<List<PlayerDto>> getAllPlayersWithSuccessRate(){
        return ResponseEntity.ok().body(playerService.getAllPlayersWithSuccessRate());
    }

    @GetMapping("/{id}/games")
    public ResponseEntity<List<GameDto>> getAllGamesByPlayerId(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok().body(playerService.getAllGamesByPlayerId(id));
    }

    @PostMapping("/{id}/games")
    public ResponseEntity<GameDto> playGame(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok().body(playerService.playGame(id));
    }

    @DeleteMapping("/{id}/games")
    public ResponseEntity<String> deleteAllGames(@PathVariable(value = "id") Long id){
        playerService.deleteAllGames(id);
        return ResponseEntity.ok().body("Deleted all games from player with ID: " + id);
    }

    @GetMapping("/ranking")
    public ResponseEntity<Double> getAvgSuccessRate(){
        return ResponseEntity.ok().body(playerService.getAvgSuccessRate());
    }
}
