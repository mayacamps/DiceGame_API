package com.itacademy.diceGame.model;

import com.itacademy.diceGame.model.dto.GameDto;
import com.itacademy.diceGame.model.dto.PlayerDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    @Test
    @DisplayName("ModelTest - Test GameDto result is win if sum of dices is 7 ")
    void gameDto_result_win_if_sum_dices_is_7(){
        GameDto gameDto = GameDto.builder().dice1(1).dice2(6).build();
        assertTrue(gameDto.hasWon());
    }

    @Test
    @DisplayName("ModelTest - Test GameDto result is lost if sum of dices is not 7 ")
    void gameDto_result_lost_if_sum_dices_is_not_7(){
        GameDto gameDto = GameDto.builder().dice1(2).dice2(6).build();
        assertFalse(gameDto.hasWon());
    }

    @Test
    @DisplayName("ModelTest - Test PlayerDto name is anonymous if null")
    void playerDto_name_anonymous_if_null(){
        PlayerDto playerDto = new PlayerDto(null, null);
        assertEquals("ANONYMOUS", playerDto.getName());
    }

}
