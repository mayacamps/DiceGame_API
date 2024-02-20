package com.itacademy.diceGame.service;

import com.itacademy.diceGame.exceptions.PlayerAlreadyExistsException;
import com.itacademy.diceGame.exceptions.PlayerNotFoundException;
import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import com.itacademy.diceGame.model.entity.Player;
import com.itacademy.diceGame.repository.PlayerRepository;
import com.itacademy.diceGame.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private List<GameDto> GameList;

    @BeforeEach
    void setUp(){
        playerWithName = Player.builder().id(1L).name("Lola").build();
        playerWithoutName = Player.builder().id(2L).name(null).build();

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
    @Order(1)
    @DisplayName("PlayerServiceTest - Test return player by id")
    void findById_should_return_player(){
        when(playerRepository.findById(1L)).thenReturn(Optional.of(playerWithName));
        Player returnedPlayer = playerService.getPlayerByID(1L);

        assertEquals(playerWithName.getId(), returnedPlayer.getId());
        assertEquals(playerWithName.getName(), returnedPlayer.getName());
        verify(playerRepository).findById(1L);
    }

    @Test
    @Order(2)
    @DisplayName("PlayerServiceTest - Test return player null name by id")
    void findById_should_return_null_name_player(){
        when(playerRepository.findById(2L)).thenReturn(Optional.of(playerWithoutName));
        Player returnedPlayer = playerService.getPlayerByID(2L);

        assertEquals(playerWithoutName.getId(), returnedPlayer.getId());
        assertNull(returnedPlayer.getName());
        verify(playerRepository).findById(2L);
    }

    @Test
    @Order(3)
    @DisplayName("PlayerServiceTest - Test getPlayerByID throws PlayerNotFoundException")
    void findById_no_should_return_player_exceptionIsThrown(){
        PlayerNotFoundException playerNotFoundException = assertThrows(PlayerNotFoundException.class,
                () -> playerService.getPlayerByID(0L));

        assertEquals("Player not found with ID: 0", playerNotFoundException.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("PlayerServiceTest - Test return player list")
    void findAll_should_return_list(){
        when(playerRepository.findAll()).thenReturn(playerList);
        List<PlayerDto> returnedPlayerList = playerService.getAllPlayersWithSuccessRate();

        assertEquals(playerList.size(), returnedPlayerList.size());
        assertTrue(new ReflectionEquals(playerList).matches(returnedPlayerList));
        verify(playerRepository).findAll();
    }

    @Test
    @Order(5)
    @DisplayName("PlayerServiceTest - Test add new player")
    void save_should_add_new_player(){
        playerService.createPlayer(new PlayerDtoRequest("maya"));

        playerList.add(Player.builder().id(3L).name("Maya").build());
        when(playerRepository.findAll()).thenReturn(playerList);

        List<PlayerDto> returnedList = playerService.getAllPlayersWithSuccessRate();
        assertEquals(playerList.get(2).getName(), returnedList.get(2).getName());
    }

    @Test
    @Order(6)
    @DisplayName("PlayerServiceTest - Test createPlayer returns PlayerAlreadyExistsException")
    void save_if_name_repeated_thenExceptionIsThrown(){
        Player playerRepeatedName = Player.builder().id(3L).name("lola").build();
        playerList.add(playerRepeatedName);

        when(playerRepository.findByNameIgnoreCase(playerRepeatedName.getName())).thenReturn(Optional.of(playerWithName));
        PlayerAlreadyExistsException playerAlreadyExistsException = assertThrows(PlayerAlreadyExistsException.class,
                () -> playerService.createPlayer(new PlayerDtoRequest("lola")));

        assertEquals("Player already exists with given name: Lola", playerAlreadyExistsException.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("PlayerServiceTest - Test update Player")
    void save_should_update_existing_player(){
        when(playerRepository.findById(1L)).thenReturn(Optional.of(playerWithName));
        when(playerRepository.save(any(Player.class))).thenReturn(playerWithName);
        playerWithName.setName("Lucas");

        PlayerDto updatedPlayer = playerService.updateNamePlayer(1L, new PlayerDtoRequest("Lucas"));
        assertEquals(playerWithName.getName(), updatedPlayer.getName());
    }

    @Test
    @Order(8)
    @DisplayName("PlayerServiceTest - Test update Player return IllegalArgumentException")
    void updatePlayerName_if_id_null_thenExceptionIsThrown() {
        PlayerDtoRequest playerDtoRequest = PlayerDtoRequest.builder().name("Sonia").build();
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> playerService.updateNamePlayer(null, playerDtoRequest));

        assertEquals("Player ID cannot be null", illegalArgumentException.getMessage());
    }


}
