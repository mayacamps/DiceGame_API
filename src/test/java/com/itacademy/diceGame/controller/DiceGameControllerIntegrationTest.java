package com.itacademy.diceGame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
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
        PlayerDtoRequest newPlayer = new PlayerDtoRequest("alexia");
        mvc.perform(post("/api/v1/dicegame/players/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPlayer)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("Player successfully added with name: " + newPlayer.getName()));
    }

    @Test
    @DisplayName("DiceGameControllerIntegrationTest - Test create new Player throws PlayerAlreadyExistsException")
    void whenCreatePlayer_thenReturnStatusConflictAndException() throws Exception {
        PlayerDtoRequest newPlayer = new PlayerDtoRequest("Lola");
        mvc.perform(post("/api/v1/dicegame/players/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPlayer)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("Player already exists with given name: " + newPlayer.getName()));
    }
}
