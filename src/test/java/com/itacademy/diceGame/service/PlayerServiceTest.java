package com.itacademy.diceGame.service;

import com.itacademy.diceGame.exceptions.PlayerAlreadyExistsException;
import com.itacademy.diceGame.exceptions.PlayerNotFoundException;
import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.repository.PlayerRepository;
import com.itacademy.diceGame.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    private List<GameDto> GameList;

    @BeforeEach
    void setUp(){
        playerWithName = Player.builder().id(1L).name("Lola").build();
        playerWithoutName = new Player(null);
        playerWithoutName.setId(2L);

        playerList = new ArrayList<>();
        playerList.add(playerWithName);
        playerList.add(playerWithoutName);

        GameList = new ArrayList<>();
        GameList.add(new GameDto(1,2));
        GameList.add(new GameDto(1,6));
        GameList.add(new GameDto(5,2));
        GameList.add(new GameDto(4,2));
    }

    @AfterEach
    void tearDown(){
        playerWithName = playerWithoutName = null;
    }

    @Test
    @DisplayName("PlayerServiceTest - Test return player by id")
    void findById_should_return_player(){
        when(playerRepository.findById(1L)).thenReturn(Optional.of(playerWithName));
        Player returnedPlayer = playerService.getPlayerByID(1L);

        assertEquals(playerWithName.getId(), returnedPlayer.getId());
        assertEquals(playerWithName.getName(), returnedPlayer.getName());
        verify(playerRepository).findById(1L);
    }

    @Test
    @DisplayName("PlayerServiceTest - Test return player default name by id")
    void findById_should_return_default_player(){
        when(playerRepository.findById(2L)).thenReturn(Optional.of(playerWithoutName));
        Player returnedPlayer = playerService.getPlayerByID(2L);

        assertEquals(playerWithoutName.getId(), returnedPlayer.getId());
        assertEquals("ANONYMOUS", returnedPlayer.getName());
        verify(playerRepository).findById(2L);
    }

    @Test
    @DisplayName("PlayerServiceTest - Test for getPlayerByID throws PlayerNotFoundException")
    void findById_no_should_return_player_exceptionIsThrown(){
        PlayerNotFoundException playerNotFoundException = assertThrows(PlayerNotFoundException.class,
                () -> playerService.getPlayerByID(0L));
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
        playerList.add(Player.builder().id(3L).name("maya").build());
        when(playerRepository.findAll()).thenReturn(playerList);

        playerService.createPlayer(new PlayerDtoRequest("maya"));

        ArgumentCaptor<PlayerDto> playerArgumentCaptor = ArgumentCaptor.forClass(PlayerDto.class);
        PlayerDto playerCreated = playerArgumentCaptor.getValue();
        assertNotNull(playerCreated);
        assertEquals("Superman", playerCreated.getName());
    }

    @Test
    @DisplayName("PlayerServiceTest - Test for createPlayer returns PlayerAlreadyExistsException")
    void save_should_not_add_player_with_name_repeated(){
        PlayerAlreadyExistsException playerAlreadyExistsException = assertThrows(PlayerAlreadyExistsException.class,
                () -> playerService.createPlayer(new PlayerDtoRequest("Lola")));
        assertEquals(playerAlreadyExistsException.getMessage(), "Player already exists with given name: Lola");
    }
}
