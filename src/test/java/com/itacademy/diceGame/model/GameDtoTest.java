package com.itacademy.diceGame.model;

import com.itacademy.diceGame.model.dto.GameDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameDtoTest {
    @Test
    @DisplayName("PlayerDtoTest - Test GameDto result is win if sum of dices is 7 ")
    void gameDto_result_win_if_sum_dices_is_7(){
        GameDto gameDto = GameDto.builder().dice1(1).dice2(6).build();
        assertTrue(gameDto.hasWon());
    }

    @Test
    @DisplayName("PlayerDtoTest - Test GameDto result is lost if sum of dices is not 7 ")
    void gameDto_result_lost_if_sum_dices_is_not_7(){
        GameDto gameDto = GameDto.builder().dice1(2).dice2(6).build();
        assertFalse(gameDto.hasWon());
    }
}
