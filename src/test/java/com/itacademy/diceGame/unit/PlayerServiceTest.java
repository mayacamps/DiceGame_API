package com.itacademy.diceGame.unit;

import com.itacademy.diceGame.exceptions.PlayerNotFoundException;
import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.repository.PlayerRepository;
import com.itacademy.diceGame.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {
    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private PlayerServiceImpl playerService;
    private Player playerWithName;
    private Player playerWithoutName;
    private List<Player> playerList;
    private List<GameDto> playerWithNameGameList;

    @BeforeEach
    void setUp(){
        playerWithName = Player.builder().id("1").name("Lola").build();
        playerWithoutName = Player.builder().id("2").build();

        playerList = new ArrayList<>();
        playerList.add(playerWithName);
        playerList.add(playerWithoutName);

        playerWithNameGameList = new ArrayList<>();
        playerWithNameGameList.add(new GameDto(1,2));
        playerWithNameGameList.add(new GameDto(1,6));
        playerWithNameGameList.add(new GameDto(5,2));
        playerWithNameGameList.add(new GameDto(4,2));
        playerWithName.setSuccessRate(50d);
    }

    @AfterEach
    void tearDown(){
        playerWithName = playerWithoutName = null;
    }

    @Test
    @DisplayName("PlayerServiceTest - Test return player by id")
    void findById_should_return_player(){
        when(playerRepository.findById("1")).thenReturn(Optional.of(playerWithName));
        Player returnedPlayer = playerService.getPlayerByID("1");

        assertEquals(playerWithName.getId(), returnedPlayer.getId());
        assertEquals(playerWithName.getName(), returnedPlayer.getName());
        verify(playerRepository).findById("1");
    }

    @Test
    @DisplayName("PlayerServiceTest - Test for getPlayerByID throws PlayerNotFoundException")
    void findById_no_should_return_player_exceptionIsThrown(){
        PlayerNotFoundException playerNotFoundException = assertThrows(PlayerNotFoundException.class,
                () -> playerService.getPlayerByID("0"));
        assertEquals(playerNotFoundException.getMessage(), "Player not found with ID: 0");
    }

    @Test
    @DisplayName("PlayerServiceTest - Test return player list")
    void findAll_should_return_list(){
        when(playerRepository.findAll()).thenReturn(playerList);
        List<PlayerDto> returnedPlayerList = playerService.getAllPlayersWithSuccessRate();

        assertEquals(playerList.size(), returnedPlayerList.size());
        assertTrue(new ReflectionEquals(playerList).matches(returnedPlayerList));
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("PlayerServiceTest - Test add new player")
    void save_should_add_new_player(){
    }
}
