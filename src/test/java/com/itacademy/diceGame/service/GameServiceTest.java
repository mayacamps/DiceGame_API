package com.itacademy.diceGame.service;

import com.itacademy.diceGame.exceptions.NoGamesSavedException;
import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.entity.Game;
import com.itacademy.diceGame.model.entity.GameHistory;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.repository.GamesRepository;
import com.itacademy.diceGame.service.impl.GamesServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTest {
    @Mock
    private GamesRepository gamesRepository;
    @InjectMocks
    private GamesServiceImpl gamesService;

    private Player player;
    private Player playerNoGames;
    private List<Player> playerList;
    private GameHistory gameHistory;
    private GameHistory gameHistoryNoGames;
    private List<Game> gameList;
    private List<Game> gameListEmpty;

    @BeforeEach
    void setUp(){
        player = Player.builder().id(1L).name("Ana").build();
        gameHistory = new GameHistory(player.getId());
        gameList = new ArrayList<>(Arrays.asList(
                new Game(3, 4), new Game(1, 6), new Game(2, 3), new Game(1, 5)));
        gameHistory.setGames(new ArrayList<>(gameList));
        gameHistory.setSuccessRate(50d);

        playerNoGames = Player.builder().id(2L).name("Pol").build();
        gameHistoryNoGames = new GameHistory(playerNoGames.getId());
        gameListEmpty = new ArrayList<>();
        gameHistoryNoGames.setGames(gameListEmpty);
        gameHistoryNoGames.setSuccessRate(null);

        playerList = new ArrayList<>(Arrays.asList(player, playerNoGames));
    }

    @AfterEach
    void tearDown(){
        player = playerNoGames = null;
    }

    @Test
    @DisplayName("GameServiceTest - Test creates new GameHistory")
    void save_should_add_gameHistory(){
        Player playerTest = Player.builder().id(3L).name("Ana").build();
        Long playerId = playerTest.getId();
        gamesService.createGameHistory(playerId);
        when(gamesRepository.findByPlayerId(playerId)).thenReturn(new GameHistory(playerId));

        verify(gamesRepository).save(any());
        Exception exception = assertThrows(NoGamesSavedException.class,
                () -> gamesService.getAllGamesDtoByPlayerId(playerId));
        assertEquals("No games saved for player with id: " + playerId, exception.getMessage());
        assertNull(gamesService.getSuccessRate(playerId));
    }

    @Test
    @DisplayName("GameServiceTest - Test return GameDto list by Id")
    void findByPlayerId_should_return_GameHistory_with_Games(){
        Long playerId = player.getId();
        when(gamesRepository.findByPlayerId(playerId)).thenReturn(gameHistory);

        List<GameDto> expectedGames = new ArrayList<>();
        gameList.forEach(game -> expectedGames.add(gamesService.gameEntityToDto(game)));
        List<GameDto> returnedGames = gamesService.getAllGamesDtoByPlayerId(playerId);

        verify(gamesRepository).findByPlayerId(playerId);
        assertNotNull(returnedGames);
        assertTrue(expectedGames.containsAll(returnedGames));
    }

    @Test
    @DisplayName("GameServiceTest - Test for getAllGamesDto throws NoGamesSavedException")
    void getAllGamesDto_if_empty_games_thenExceptionIsThrown(){
        Long playerId = playerNoGames.getId();
        when(gamesRepository.findByPlayerId(playerId)).thenReturn(gameHistoryNoGames);

        assertThrows(NoGamesSavedException.class,
                () -> gamesService.getAllGamesDtoByPlayerId(playerId));

        verify(gamesRepository).findByPlayerId(playerId);
    }

    @RepeatedTest(10)
    @DisplayName("GameServiceTest - Test playGame when empty games")
    void playGame_on_empty_GameHistory(){
        Long playerId = playerNoGames.getId();
        when(gamesRepository.findByPlayerId(playerId)).thenReturn(gameHistoryNoGames);
        GameDto returnedGame = gamesService.playGame(playerId);
        double expectedSuccessRate = 0d;
        if (returnedGame.hasWon()){
            expectedSuccessRate = 100d;
        }

        verify(gamesRepository).findByPlayerId(playerId);
        assertNotNull(returnedGame);
        assertEquals(expectedSuccessRate, gameHistoryNoGames.getSuccessRate());
    }

    @RepeatedTest(10)
    @DisplayName("GameServiceTest - Test playGame for updated successRate")
    void playGame_updates_success_rate(){
        Long playerId = player.getId();
        when(gamesRepository.findByPlayerId(playerId)).thenReturn(gameHistory);
        GameDto returnedGame = gamesService.playGame(playerId);
        double expectedSuccessRate = 40d;
        if (returnedGame.hasWon()){
            expectedSuccessRate = 60d;
        }

        verify(gamesRepository).findByPlayerId(playerId);
        assertNotNull(returnedGame);
        assertEquals(expectedSuccessRate, gameHistory.getSuccessRate());
    }
}
