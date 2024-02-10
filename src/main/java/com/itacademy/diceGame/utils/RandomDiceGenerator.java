package com.itacademy.diceGame.utils;

public class RandomDiceGenerator {
    private final static int MIN = 1;
    private final static int MAX = 6;

    public static int throwDice(){
        return (int) (Math.random() * (MAX - MIN + 1) + MIN);
    }
}
