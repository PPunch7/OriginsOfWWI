package com.example.originsofworldwari;

import java.util.Random;

public class Dice {
    private static Dice instance;
    private static int maxNum;
    private int currentNum;

    private Dice(){
        maxNum = 6;
    }

    public static Dice getInstance(){
        if (instance == null)
            instance = new Dice();
        return instance;
    }

    public int getNum(){
        return this.currentNum;
    }

    public void random(){
        Random r = new Random();
        this.currentNum = (r.nextInt(maxNum) + 1);
    }
}
