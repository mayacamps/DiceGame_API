package com.itacademy.diceGame.controller;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import com.itacademy.diceGame.service.PlayerService;
import jakarta.validation.Valid;
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

    @PostMapping("/")
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody @Valid PlayerDto playerDto){
        return ResponseEntity.ok().body(playerService.createPlayer(playerDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDto> updateNamePlayer(@PathVariable(value = "id") Long id, @RequestBody @Valid PlayerDtoRequest playerDtoRequest){
        return ResponseEntity.ok().body(playerService.updateNamePlayer(id, playerDtoRequest));
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

    @GetMapping("/ranking/winner")
    public ResponseEntity<List<PlayerDto>> getWinner(){
        return ResponseEntity.ok().body(playerService.getWinner());
    }

    @GetMapping("/ranking/loser")
    public ResponseEntity<List<PlayerDto>> getLoser(){
        return ResponseEntity.ok().body(playerService.getLoser());
    }
}
