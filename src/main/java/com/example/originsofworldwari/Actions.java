package com.example.originsofworldwari;

import java.util.HashMap;

public class Actions implements Comparable<Actions>{
    private Players player;
    private HashMap<Nations, Integer> placePF = new HashMap<Nations, Integer>();
    private HashMap<Nations, Players> attack = new HashMap<Nations, Players>();
    private int score;

    public Actions(Players player){
        this.player = player;
    }

    public void setPlace(Nations location, int pf){
        this.placePF.put(location, pf);
    }

    public HashMap<Nations, Integer> getPlacePF() { return this.placePF; }

    public void setAttack(Nations location, Players def) { this.attack.put(location, def); }

    public HashMap<Nations, Players> getAttack() { return  this.attack; }

    public void setScore(int score) { this.score = score; }

    public int getScore() { return this.score; }

    @Override
    public int compareTo(Actions o) {
        int compareScore = ((Actions) o).getScore();
        return compareScore - getScore();
    }
}
