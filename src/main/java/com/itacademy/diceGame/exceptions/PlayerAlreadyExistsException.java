package com.itacademy.diceGame.exceptions;

public class PlayerAlreadyExistsException  extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}
