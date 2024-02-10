package com.itacademy.diceGame.exceptions;

public class NoGamesSavedException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public NoGamesSavedException(String message){super(message);}
}
