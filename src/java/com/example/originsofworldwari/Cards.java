package com.example.originsofworldwari;

public class Cards {
    private int id;
    private String nations;
    private int numPF;

    public Cards(int id_input, String name, int numPF_input){
        this.id = id_input;
        this.nations = name;
        this.numPF = numPF_input;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.nations;
    }

    public int getNumPF(){
        return  this.numPF;
    }
}
