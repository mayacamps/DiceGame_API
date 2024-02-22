package com.itacademy.diceGame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DiceGameControllerIntegrationTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test return Player list")
    void whenGetPlayers_thenReturnStatusOKAndPLayerList() throws Exception {
        mvc.perform(get("/api/v1/dicegame/players/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test create new Player")
    void whenCreatePlayer_thenReturnStatusCreated() throws Exception {
        PlayerDtoRequest newPlayer = new PlayerDtoRequest("Alexia");
        mvc.perform(post("/api/v1/dicegame/players/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPlayer)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("Player successfully added with name: " + newPlayer.getName()));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test create new Player")
    void givenEmptyName_whenCreatePlayer_thenReturnStatusBadRequest() throws Exception {
        PlayerDtoRequest newPlayer = new PlayerDtoRequest("");
        mvc.perform(post("/api/v1/dicegame/players/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPlayer)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("name: Introduce a name. Cannot be more than 15 characters long."));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test create new Player")
    void givenNameTooLong_whenCreatePlayer_thenReturnStatusBadRequest() throws Exception {
        PlayerDtoRequest newPlayer = new PlayerDtoRequest("aaaaaaaaaaaaaaaaaaaa");
        mvc.perform(post("/api/v1/dicegame/players/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPlayer)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("name: Introduce a name. Cannot be more than 15 characters long."));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test create new Player throws PlayerAlreadyExistsException")
    void givenPlayerExist_whenCreatePlayer_thenReturnStatusConflictAndException() throws Exception {
        PlayerDtoRequest newPlayer = new PlayerDtoRequest("Lola");
        mvc.perform(post("/api/v1/dicegame/players/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPlayer)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("Player already exists with given name: " + newPlayer.getName()));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test update Player")
    void whenUpdatePlayer_thenReturnsStatusOk() throws Exception {
        Long idPlayer = 2L;
        PlayerDtoRequest updatedPlayer = new PlayerDtoRequest("Sandy");
        mvc.perform(put("/api/v1/dicegame/players/{id}", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPlayer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Player successfully updated with name: " + updatedPlayer.getName()));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test update Player throws PlayerAlreadyExistsException")
    void givenPlayerExist_whenUpdatePlayer_thenReturnsStatusConflictAndException() throws Exception {
        Long idPlayer = 2L;
        PlayerDtoRequest updatedPlayer = new PlayerDtoRequest("Lucas");
        mvc.perform(put("/api/v1/dicegame/players/{id}", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPlayer)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("Player already exists with given name: " + updatedPlayer.getName()));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test update Player throws PlayerNotFoundException")
    void givenPlayerNotExist_whenUpdatePlayer_thenReturnsStatusNotFoundAndException() throws Exception {
        Long idPlayer = -1L;
        PlayerDtoRequest updatedPlayer = new PlayerDtoRequest("Lucas");
        mvc.perform(put("/api/v1/dicegame/players/{id}", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPlayer)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Player not found with ID: " + idPlayer));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test return Game list by id")
    void whenGetAllGames_thenReturnsStatusOkAndGameDtoList() throws Exception {
        Long idPlayer = 2L;
        mvc.perform(get("/api/v1/dicegame/players/{id}/games", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test return Game list throws PlayerNotFoundException")
    void givenPlayerNotExist_whenGetAllGames_thenReturnsStatusNotFoundAndException() throws Exception {
        Long idPlayer = -1L;
        mvc.perform(get("/api/v1/dicegame/players/{id}/games", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Player not found with ID: " + idPlayer));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test return Game list throws NoGamesSavedException")
    void givenPlayerWithNoGames_whenGetAllGames_thenReturnsStatusNotFoundAndException() throws Exception {
        Long idPlayer = 1L;
        mvc.perform(get("/api/v1/dicegame/players/{id}/games", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No games saved for player with id: " + idPlayer));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test play Game list by id")
    void whenPlayGame_thenReturnsStatusOkAndGameDto() throws Exception {
        Long idPlayer = 4L;
        mvc.perform(post("/api/v1/dicegame/players/{id}/games", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dice1").isNumber())
                .andExpect(jsonPath("$.dice2").isNumber())
                .andExpect(jsonPath("$.result").isString())
                .andExpect(jsonPath("$.result", Matchers.anyOf(
                        Matchers.is("WIN"),
                        Matchers.is("LOST"))));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test play Game throws PlayerNotFoundException")
    void givenPlayerNotExist_whenPlayGame_thenReturnsStatusNotFoundAndException() throws Exception {
        Long idPlayer = -1L;
        mvc.perform(post("/api/v1/dicegame/players/{id}/games", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Player not found with ID: " + idPlayer));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test delete all Games")
    void whenDeleteAllGames_thenReturnsStatusOk() throws Exception {
        Long idPlayer = 2L;
        mvc.perform(delete("/api/v1/dicegame/players/{id}/games", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$").value("Deleted all games from player with ID: " + idPlayer));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test delete all Games by Id throws PlayerNotFoundException")
    void givenPlayerNotExist_whenDeleteAllGames_thenReturnsStatusNotFoundAndException() throws Exception {
        Long idPlayer = -1L;
        mvc.perform(delete("/api/v1/dicegame/players/{id}/games", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Player not found with ID: " + idPlayer));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test delete all Games by Id throws NoGamesSavedException")
    void givenPlayerWithNoGames_whenDeleteAllGames_thenReturnsStatusNotFoundAndException() throws Exception {
        Long idPlayer = 1L;
        mvc.perform(delete("/api/v1/dicegame/players/{id}/games", idPlayer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No games saved for player with id: " + idPlayer));
    }

    @Test
    @Order(1)
    @DisplayName("DiceGameControllerIntegrationTest - Test get Avg Success Rate")
    void whenGetAvgSuccessRate_thenReturnsStatusOkAndDouble() throws Exception {
        mvc.perform(get("/api/v1/dicegame/players/ranking")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(50d));
    }

    @Test
    @Order(2)
    @DisplayName("DiceGameControllerIntegrationTest - Test get Winner")
    void whenGetWinner_thenReturnsStatusOkAndPlayerDtoList() throws Exception {
        mvc.perform(get("/api/v1/dicegame/players/ranking/winner")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("ANONYMOUS")))
                .andExpect(jsonPath("$[0].successRate", Matchers.is(100d)));
    }

    @Test
    @Order(3)
    @DisplayName("DiceGameControllerIntegrationTest - Test get Loser")
    void whenGetLoser_thenReturnsStatusOkAndPlayerDtoList() throws Exception {
        mvc.perform(get("/api/v1/dicegame/players/ranking/loser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Pol")))
                .andExpect(jsonPath("$[0].successRate", Matchers.is(25d)))
                .andExpect(jsonPath("$[1].name", Matchers.is("Lucas")))
                .andExpect(jsonPath("$[1].successRate", Matchers.is(25d)));
    }

}
