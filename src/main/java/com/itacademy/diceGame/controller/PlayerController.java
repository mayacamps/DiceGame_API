package com.itacademy.diceGame.controller;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import com.itacademy.diceGame.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dicegame/players")
public class PlayerController {
    private final PlayerService playerService;

    @Operation(summary = "Show All Players with their Success Rate")
    @GetMapping("/")
    public ResponseEntity<List<PlayerDto>> getAllPlayersWithSuccessRate(){
        return ResponseEntity.ok().body(playerService.getAllPlayersWithSuccessRate());
    }

    @Operation(summary = "Create new Player")
    @PostMapping("/")
    public ResponseEntity<String> createPlayer(@RequestBody @Valid PlayerDtoRequest playerDtoRequest){
        playerService.createPlayer(playerDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Player successfully added with name: " + playerDtoRequest.getName());
    }

    @Operation(summary = "Update Player's name")
    @PutMapping("/{id}")
    public ResponseEntity<PlayerDto> updateNamePlayer(@PathVariable(value = "id") Long id, @RequestBody @Valid PlayerDtoRequest playerDtoRequest){
        return ResponseEntity.ok().body(playerService.updateNamePlayer(id, playerDtoRequest));
    }

    @Operation(summary = "Get All Games of Player")
    @GetMapping("/{id}/games")
    public ResponseEntity<List<GameDto>> getAllGamesByPlayerId(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok().body(playerService.getAllGamesByPlayerId(id));
    }

    @Operation(summary = "Player plays Game")
    @PostMapping("/{id}/games")
    public ResponseEntity<GameDto> playGame(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok().body(playerService.playGame(id));
    }

    @Operation(summary = "Delete All Games of Player")
    @DeleteMapping("/{id}/games")
    public ResponseEntity<String> deleteAllGames(@PathVariable(value = "id") Long id){
        playerService.deleteAllGames(id);
        return ResponseEntity.ok().body("Deleted all games from player with ID: " + id);
    }

    @Operation(summary = "Get Average Success Rate of All Players")
    @GetMapping("/ranking")
    public ResponseEntity<Double> getAvgSuccessRate(){
        return ResponseEntity.ok().body(playerService.getAvgSuccessRate());
    }

    @Operation(summary = "Get Winner - Player/s with highest score")
    @GetMapping("/ranking/winner")
    public ResponseEntity<List<PlayerDto>> getWinner(){
        return ResponseEntity.ok().body(playerService.getWinner());
    }

    @Operation(summary = "Get Loser - Player/s with lowest score")
    @GetMapping("/ranking/loser")
    public ResponseEntity<List<PlayerDto>> getLoser(){
        return ResponseEntity.ok().body(playerService.getLoser());
    }
}
