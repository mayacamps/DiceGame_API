package com.itacademy.diceGame.model;

import com.itacademy.diceGame.model.dto.request.PlayerDtoRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerDtoRequestTest {
    private Validator validator;

    @BeforeEach
    public void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("PlayerDtoRequestTest - Test name longer than requirement fails validation")
    public void playerRequestDto_fails_validation_if_name_longer_than_valid(){
        int invalidLength = 16;
        String randomName = RandomStringUtils.random(invalidLength);
        PlayerDtoRequest playerDtoRequest = new PlayerDtoRequest(randomName);
        Set<ConstraintViolation<PlayerDtoRequest>> violations = validator.validate(playerDtoRequest);
        assertFalse(violations.isEmpty());
    }

    @RepeatedTest(5)
    @DisplayName("PlayerDtoRequestTest - Test name is validated")
    public void playerRequestDto_validates_name(){
        int maxLength = 15;
        int minLength = 1;
        int randomValidLength = new Random().nextInt((maxLength - minLength) + 1) + minLength;

        String randomName = RandomStringUtils.random(randomValidLength);
        PlayerDtoRequest playerDtoRequest = new PlayerDtoRequest(randomName);
        Set<ConstraintViolation<PlayerDtoRequest>> violations = validator.validate(playerDtoRequest);
        assertTrue(violations.isEmpty());
    }
}
