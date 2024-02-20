package com.itacademy.diceGame.model;

import com.itacademy.diceGame.model.dto.PlayerDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerDtoTest {
    @Test
    @DisplayName("PlayerDtoTest - Test PlayerDto name is anonymous if null")
    void playerDto_name_anonymous_if_null(){
        PlayerDto playerDto = new PlayerDto(null, null);
        assertEquals("ANONYMOUS", playerDto.getName());
    }
}
