package com.itacademy.diceGame.utils;

import com.itacademy.diceGame.utils.RandomDiceGenerator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilsTest {
    @DisplayName("UtilsTest - Test random dice generator")
    @RepeatedTest(30)
    void throwDice_should_return_int_between_1_6(){
        int number = RandomDiceGenerator.throwDice();
        assertTrue(number >= 1 && number <= 6);
    }
}
