package com.itacademy.diceGame.service;

import com.itacademy.diceGame.exceptions.NoGamesSavedException;
import com.itacademy.diceGame.exceptions.PlayerAlreadyExistsException;
import com.itacademy.diceGame.exceptions.PlayerNotFoundException;
import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.repository.PlayerRepository;
import com.itacademy.diceGame.service.impl.PlayerServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerServiceTest {
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GamesService gamesService;
    @InjectMocks
    private PlayerServiceImpl playerService;

    private Player playerWithName;
    private Player playerWithoutName;
    private List<Player> playerList;

    @BeforeEach
    void setUp(){
        playerWithName = Player.builder().id(1L).name("Lola").build();
        playerWithoutName = Player.builder().id(2L).name(null).build();

        playerList = new ArrayList<>(Arrays.asList(playerWithName, playerWithoutName));
    }

    @AfterEach
    void tearDown(){
        playerWithName = playerWithoutName = null;
    }

    @Test @Order(1)
    @DisplayName("PlayerServiceTest - Test return player by id")
    void findById_should_return_player(){
        when(playerRepository.findById(1L)).thenReturn(Optional.ofNullable(playerWithName));
        Player returnedPlayer = playerService.getPlayerByID(1L);

        assertNotNull(returnedPlayer);
        assertEquals(playerWithName.getId(), returnedPlayer.getId());
        assertEquals(playerWithName.getName(), returnedPlayer.getName());
        verify(playerRepository).findById(1L);
    }

    @Test @Order(2)
    @DisplayName("PlayerServiceTest - Test return player null name by id")
    void findById_should_return_null_name_player(){
        when(playerRepository.findById(2L)).thenReturn(Optional.ofNullable(playerWithoutName));
        Player returnedPlayer = playerService.getPlayerByID(2L);

        assertNotNull(returnedPlayer);
        assertEquals(playerWithoutName.getId(), returnedPlayer.getId());
        assertNull(returnedPlayer.getName());
        verify(playerRepository).findById(2L);
    }

    @Test @Order(3)
    @DisplayName("PlayerServiceTest - Test getPlayerByID throws PlayerNotFoundException")
    void findById_no_should_return_player_exceptionIsThrown(){
        Exception exception = assertThrows(PlayerNotFoundException.class,
                () -> playerService.getPlayerByID(-1L));
        assertEquals("Player not found with ID: -1", exception.getMessage());
        verify(playerRepository).findById(-1L);
    }

    @Test @Order(4)
    @DisplayName("PlayerServiceTest - Test return player list")
    void findAll_should_return_list(){
        when(playerRepository.findAll()).thenReturn(playerList);
        List<PlayerDto> playerDtoList = new ArrayList<>();
        playerList.forEach(player -> playerDtoList.add(playerService.playerEntityToDto(player)));
        List<PlayerDto> returnedPlayerList = playerService.getAllPlayersWithSuccessRate();

        assertEquals(playerList.size(), returnedPlayerList.size());
        assertTrue(playerDtoList.containsAll(returnedPlayerList));
        verify(playerRepository).findAll();
    }

    @Test @Order(5)
    @DisplayName("PlayerServiceTest - Test add new player")
    void save_should_add_new_player(){
        playerService.createPlayer(new PlayerDtoRequest("maya"));
        playerList.add(Player.builder().id(3L).name("Maya").build());
        when(playerRepository.findAll()).thenReturn(playerList);

        List<PlayerDto> returnedList = playerService.getAllPlayersWithSuccessRate();
        assertEquals(3, returnedList.size());
        assertEquals("Maya", returnedList.get(2).getName());
        verify(playerRepository).save(any());
        verify(gamesService).createGameHistory(any());
    }

    @Test @Order(6)
    @DisplayName("PlayerServiceTest - Test createPlayer throws PlayerAlreadyExistsException")
    void save_if_name_repeated_thenExceptionIsThrown(){
        String repeatedName = "lola";
        when(playerRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        Exception exception = assertThrows(PlayerAlreadyExistsException.class,
                () -> playerService.createPlayer(new PlayerDtoRequest(repeatedName)));
        assertEquals("Player already exists with given name: Lola", exception.getMessage());
        verify(gamesService, never()).createGameHistory(any());
    }

    @Test @Order(7)
    @DisplayName("PlayerServiceTest - Test update Player")
    void save_should_update_existing_player(){
        String newName = "alberto";
        when(playerRepository.findById(1L)).thenReturn(Optional.of(playerWithName));
        when(playerRepository.findByNameIgnoreCase(newName)).thenReturn(Optional.empty());
        when(playerRepository.save(any(Player.class))).thenReturn(playerWithName);

        playerService.updateNamePlayer(1L, new PlayerDtoRequest(newName));

        assertEquals(StringUtils.capitalize(newName), playerWithName.getName());
        verify(playerRepository).findById(1L);
        verify(playerRepository).findByNameIgnoreCase(newName);
        verify(playerRepository).save(playerWithName);
    }

    @Test @Order(8)
    @DisplayName("PlayerServiceTest - Test update Player")
    void save_should_update_existing_player_if_maintain_name(){
        String sameName = "lola";
        when(playerRepository.findById(1L)).thenReturn(Optional.of(playerWithName));
        when(playerRepository.save(any(Player.class))).thenReturn(playerWithName);

        playerService.updateNamePlayer(1L, new PlayerDtoRequest(sameName));

        assertEquals(StringUtils.capitalize(sameName), playerWithName.getName());
        verify(playerRepository).findById(1L);
        verify(playerRepository).save(playerWithName);
    }

    @Test @Order(9)
    @DisplayName("PlayerServiceTest - Test update Player throws IllegalArgumentException")
    void updatePlayerName_if_id_null_thenExceptionIsThrown() {
        String newName = "Sonia";

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> playerService.updateNamePlayer(null, new PlayerDtoRequest(newName)));
        assertEquals("Player ID cannot be null", exception.getMessage());
    }

    @Test @Order(10)
    @DisplayName("PlayerServiceTest - Test update Player throws PlayerNotFoundException")
    void updatePlayerName_no_should_update_exceptionIsThrown(){
        String newName = "Sonia";

        Exception exception = assertThrows(PlayerNotFoundException.class,
                () -> playerService.updateNamePlayer(-1L, new PlayerDtoRequest(newName)));
        assertEquals("Player not found with ID: -1", exception.getMessage());
        verify(playerRepository).findById(-1L);
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test @Order(11)
    @DisplayName("PlayerServiceTest - Test update Player throws PlayerAlreadyExistsException")
    void updatePlayerName_if_name_repeated_thenExceptionIsThrown() {
        String repeatedName = "lola";
        when(playerRepository.findById(2L)).thenReturn(Optional.of(playerWithoutName));
        when(playerRepository.findByNameIgnoreCase(repeatedName)).thenReturn(Optional.of(playerWithName));

        Exception exception = assertThrows(PlayerAlreadyExistsException.class,
                () -> playerService.updateNamePlayer(2L, new PlayerDtoRequest(repeatedName)));
        assertEquals("Player already exists with given name: Lola", exception.getMessage());
    }

    @Test @Order(12)
    @DisplayName("PlayerServiceTest - Test get all Games by id")
    void getAllGames_calls_GameService_method(){
        List<GameDto> expectedGameDtos = new ArrayList<>(Arrays.asList(
                new GameDto(1,2),
                new GameDto(3,4)
        ));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(playerWithName));
        when(gamesService.getAllGamesDtoByPlayerId(1L)).thenReturn(expectedGameDtos);

        List<GameDto> returnedGames = playerService.getAllGamesByPlayerId(1L);

        assertTrue(expectedGameDtos.containsAll(returnedGames));
        verify(playerRepository).findById(1L);
        verify(gamesService).getAllGamesDtoByPlayerId(1L);
    }

    @Test @Order(13)
    @DisplayName("PlayerServiceTest - Test get all Games throws PlayerNotFoundException")
    void getAllGames_no_should_call_gameService_exceptionIsThrown(){
        Exception exception = assertThrows(PlayerNotFoundException.class,
                () -> playerService.getAllGamesByPlayerId(-1L));
        assertEquals("Player not found with ID: -1", exception.getMessage());
        verify(playerRepository).findById(-1L);
        verify(gamesService, never()).getAllGamesDtoByPlayerId(-1L);
    }

    @Test @Order(14)
    @DisplayName("PlayerServiceTest - Test play Game by id")
    void playGame_calls_GameService_method(){
        GameDto expectedGameDto = new GameDto(1,4);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(playerWithName));
        when(gamesService.playGame(1L)).thenReturn(expectedGameDto);

        GameDto returnedGameDto = playerService.playGame(1L);
        assertEquals(expectedGameDto, returnedGameDto);
        verify(playerRepository).findById(1L);
        verify(gamesService).playGame(1L);
    }

    @Test @Order(15)
    @DisplayName("PlayerServiceTest - Test play Game throws PlayerNotFoundException")
    void playGame_no_should_call_gameService_exceptionIsThrown(){
        Exception exception = assertThrows(PlayerNotFoundException.class,
                () -> playerService.getAllGamesByPlayerId(-1L));
        assertEquals("Player not found with ID: -1", exception.getMessage());
        verify(playerRepository).findById(-1L);
        verify(gamesService, never()).playGame(-1L);
    }

    @Test @Order(16)
    @DisplayName("PlayerServiceTest - Test delete all Games by id")
    void deleteAllGames_calls_GameService_method(){
        when(playerRepository.findById(1L)).thenReturn(Optional.of(playerWithName));

        playerService.deleteAllGames(1L);

        verify(playerRepository).findById(1L);
        verify(gamesService).deleteAllGames(1L);
    }

    @Test @Order(17)
    @DisplayName("PlayerServiceTest - Test delete all Games throws PlayerNotFoundException")
    void deleteAllGames_no_should_call_gameService_exceptionIsThrown(){
        Exception exception = assertThrows(PlayerNotFoundException.class,
                () -> playerService.getAllGamesByPlayerId(-1L));
        assertEquals("Player not found with ID: -1", exception.getMessage());
        verify(playerRepository).findById(-1L);
        verify(gamesService, never()).deleteAllGames(-1L);
    }
    
    @Test @Order(18)
    @DisplayName("PlayerServiceTest - Test Avg Success Rate")
    void getAvgSuccessRate_should_return_double(){
        playerList.add(Player.builder().id(3L).name("Ana").build());
        when(playerRepository.findAll()).thenReturn(playerList);
        when(gamesService.getSuccessRate(1L)).thenReturn(50d);
        when(gamesService.getSuccessRate(2L)).thenReturn(75d);
        when(gamesService.getSuccessRate(3L)).thenReturn(null);

        double expectedAvg = (50d + 75d) / 2;
        double returnedAvg = playerService.getAvgSuccessRate();

        assertEquals(expectedAvg, returnedAvg);
        verify(playerRepository).findAll();
        verify(gamesService, times(playerList.size())).getSuccessRate(any());
    }

    @Test @Order(19)
    @DisplayName("PlayerServiceTest - Test getAvgSuccessRate throws NoGamesSavedException")
    void getAvgSuccessRate_if_games_empty_thenExceptionIsThrown(){
        when(playerRepository.findAll()).thenReturn(playerList);
        when(gamesService.getSuccessRate(1L)).thenReturn(null);
        when(gamesService.getSuccessRate(2L)).thenReturn(null);

        Exception exception = assertThrows(NoGamesSavedException.class,
                () -> playerService.getAvgSuccessRate());
        assertEquals("There are no games saved.", exception.getMessage());
        verify(playerRepository).findAll();
        verify(gamesService, times(playerList.size())).getSuccessRate(any());
    }

    @Test @Order(20)
    @DisplayName("PlayerServiceTest - Test get Winner")
    void getWinner_should_return_only_1_PlayerDto(){
        playerList.add(Player.builder().id(3L).name("Ana").build());
        when(playerRepository.findAll()).thenReturn(playerList);
        when(gamesService.getSuccessRate(1L)).thenReturn(50d);
        when(gamesService.getSuccessRate(2L)).thenReturn(75d);
        when(gamesService.getSuccessRate(3L)).thenReturn(null);

        PlayerDto expectedWinner = new PlayerDto(null, 75d);
        PlayerDto returnedWinner = playerService.getWinner().get(0);

        assertEquals(expectedWinner, returnedWinner);
        verify(playerRepository).findAll();
        verify(gamesService, times(playerList.size())).getSuccessRate(any());
    }

    @Test @Order(21)
    @DisplayName("PlayerServiceTest - Test gets Winners tied")
    void getWinner_should_return_winners_tie(){
        playerList.add(Player.builder().id(3L).name("Ana").build());
        when(playerRepository.findAll()).thenReturn(playerList);
        when(gamesService.getSuccessRate(1L)).thenReturn(50d);
        when(gamesService.getSuccessRate(2L)).thenReturn(50d);
        when(gamesService.getSuccessRate(3L)).thenReturn(25d);

        List<PlayerDto> expectedWinners = Arrays.asList(
                new PlayerDto("lola", 50d),
                new PlayerDto(null, 50d));
        List<PlayerDto> returnedWinners = playerService.getWinner();

        assertTrue(expectedWinners.containsAll(returnedWinners));
        verify(playerRepository).findAll();
        verify(gamesService, times(playerList.size())).getSuccessRate(any());
    }

    @Test @Order(22)
    @DisplayName("PlayerServiceTest - Test getWinner throws NoGamesSavedException")
    void getWinner_if_games_empty_thenExceptionIsThrown(){
        when(playerRepository.findAll()).thenReturn(playerList);
        when(gamesService.getSuccessRate(1L)).thenReturn(null);
        when(gamesService.getSuccessRate(2L)).thenReturn(null);

        Exception exception = assertThrows(NoGamesSavedException.class,
                () -> playerService.getAvgSuccessRate());
        assertEquals("There are no games saved.", exception.getMessage());
        verify(playerRepository).findAll();
        verify(gamesService, times(playerList.size())).getSuccessRate(any());
    }

    @Test @Order(23)
    @DisplayName("PlayerServiceTest - Test gets Loser")
    void getLoser_should_return_only_1_PlayerDto(){
        playerList.add(Player.builder().id(3L).name("Ana").build());
        when(playerRepository.findAll()).thenReturn(playerList);
        when(gamesService.getSuccessRate(1L)).thenReturn(50d);
        when(gamesService.getSuccessRate(2L)).thenReturn(75d);
        when(gamesService.getSuccessRate(3L)).thenReturn(null);

        PlayerDto expectedLoser = new PlayerDto("lola", 50d);
        PlayerDto returnedLoser = playerService.getLoser().get(0);

        assertEquals(expectedLoser, returnedLoser);
        verify(playerRepository).findAll();
        verify(gamesService, times(playerList.size())).getSuccessRate(any());
    }

    @Test @Order(24)
    @DisplayName("PlayerServiceTest - Test get Losers tied")
    void getLoser_should_return_losers_tie(){
        playerList.add(Player.builder().id(3L).name("Ana").build());
        when(playerRepository.findAll()).thenReturn(playerList);
        when(gamesService.getSuccessRate(1L)).thenReturn(50d);
        when(gamesService.getSuccessRate(2L)).thenReturn(25d);
        when(gamesService.getSuccessRate(3L)).thenReturn(25d);

        List<PlayerDto> expectedLosers = Arrays.asList(
                new PlayerDto(null, 25d),
                new PlayerDto("Ana", 25d));
        List<PlayerDto> returnedLosers = playerService.getLoser();

        assertTrue(expectedLosers.containsAll(returnedLosers));
        verify(playerRepository).findAll();
        verify(gamesService, times(playerList.size())).getSuccessRate(any());
    }

    @Test @Order(25)
    @DisplayName("PlayerServiceTest - Test getLoser throws NoGamesSavedException")
    void getLoser_if_games_empty_thenExceptionIsThrown(){
        when(playerRepository.findAll()).thenReturn(playerList);
        when(gamesService.getSuccessRate(1L)).thenReturn(null);
        when(gamesService.getSuccessRate(2L)).thenReturn(null);

        Exception exception = assertThrows(NoGamesSavedException.class,
                () -> playerService.getAvgSuccessRate());
        assertEquals("There are no games saved.", exception.getMessage());
        verify(playerRepository).findAll();
        verify(gamesService, times(playerList.size())).getSuccessRate(any());
    }
}