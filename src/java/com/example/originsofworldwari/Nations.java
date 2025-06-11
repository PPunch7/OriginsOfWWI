package com.example.originsofworldwari;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Nations {
    private int id;
    private String name;
    private boolean canPlay;
    private HashMap<Players, Integer> pf;
    private ArrayList<Players> treatyRight;

    public Nations(int id_input, String name_input, boolean canPlay_input){
        this.id = id_input;
        this.name = name_input;
        this.canPlay = canPlay_input;
        this.pf = new HashMap<Players, Integer>();
        this.treatyRight = new ArrayList<Players>();
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public boolean getCanPlay(){
        return this.canPlay;
    }

    public void setPF(Players player, int pf){
        Iterator<Map.Entry<Players, Integer>> iterator = this.pf.entrySet().iterator();
        boolean isKeyPresent = false;
        while (iterator.hasNext()){
            Map.Entry<Players, Integer> entry = iterator.next();
            if (entry.getKey().equals(player)) {
                isKeyPresent = true;
                break;
            }
        }
        if (isKeyPresent)
            this.pf.put(player, this.pf.get(player) + pf);
        else
            this.pf.put(player, pf);
    }

    public int getPF(Players player){
        if (pf.containsKey(player))
            return this.pf.get(player);
        else
            return 0;
    }

    public HashMap<Players, Integer> getAllPF() { return this.pf; }

    public void clearPF() { this.pf.clear(); this.treatyRight.clear(); }

    public void addTreatyRight(Players player){
        if (!(this.treatyRight.contains(player)))
            this.treatyRight.add(player);
    }

    public void removeTreatyRight(Players player){
        if (this.treatyRight.contains(player))
            this.treatyRight.remove(player);
    }

    public ArrayList<Players> getTreatyRight(){
        return this.treatyRight;
    }
}
