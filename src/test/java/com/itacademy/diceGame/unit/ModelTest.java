package com.itacademy.diceGame.unit;

import com.itacademy.diceGame.model.dto.GameDto;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    @DisplayName("ModelTest - Test GameDto result is win if sum of dices is 7 ")
    @Test
    void gameDto_result_win_if_sum_dices_is_7(){
        GameDto gameDto = GameDto.builder().dice1(1).dice2(6).build();
        assertTrue(gameDto.hasWon());
    }

    @DisplayName("ModelTest - Test GameDto result is lost if sum of dices is not 7 ")
    @Test
    void gameDto_result_lost_if_sum_dices_is_not_7(){
        GameDto gameDto = GameDto.builder().dice1(2).dice2(6).build();
        assertFalse(gameDto.hasWon());
    }
}
