package com.example.originsofworldwari;

public class Players implements Comparable<Players>{
    private int id;
    private String name;
    private String colour;
    private Cards cardSelected;
    private int totalTurn;
    private int totalScore;
    private boolean isAI;
    private String difficulty;

    public Players(int id_input, String name_input, String colour_input, boolean isAI_input){
        this.id = id_input;
        this.name = name_input;
        this.colour = colour_input;
        this.totalTurn = 0;
        this.totalScore = 0;
        this.isAI = isAI_input;
    }

    public int getId(){
        return this.id;
    }

    public void setName(String name) { this.name = name; }

    public String getName(){
        return this.name;
    }

    public String getColour(){
        return this.colour;
    }

    public void selectCard(Cards card){
        this.cardSelected = card;
    }

    public Cards getCardSelected(){
        return this.cardSelected;
    }

    public void setTotalTurn(){
        this.totalTurn ++;
    }

    public int getTotalTurn(){
        return this.totalTurn;
    }

    public void clearTotalTurn() { this.totalTurn = 0; }

    public void setTotalScore(int totalScore){
        this.totalScore = totalScore;
    }

    public int getTotalScore(){
        return this.totalScore;
    }

    public void setIsAI(boolean is) { this.isAI = is; }

    public boolean getIsAI() { return  this.isAI; }

    public void setDifficulty(String level) { this.difficulty = level; }

    public String getDifficulty() { return this.difficulty; }

    public void placePF(String location, int pf){
        Game.getInstance().placePF(location, this, pf);
    }

    public void attack(Nations location, Players def){
        Game.getInstance().calAttack(location, this, def);
    }

    public int tollDice(){
        Dice dice = Dice.getInstance();
        dice.random();
        return dice.getNum();
    }

    @Override
    public int compareTo(Players o) {
        int compareScore = ((Players) o).getTotalScore();
        return compareScore - getTotalScore();
    }

    //******************* For AI movement ***********************

    /** Simple decision **/ // just calculate score
    public Actions autoAction1(Board board){
        Minimax mnm = new Minimax();
        return mnm.simpleDecision(board, this);
    }

    /** Minimax improvement **/ // give score for different opportunities and go deep in search tree
    public Actions autoAction2(Board board, int maxDeep, int maxChance, boolean writeLog){
        Minimax mnm = new Minimax();
        return mnm.minimaxDecision(board, this, maxDeep, maxChance, writeLog);
    }

    /** Pure minimax **/
    public Actions autoAction3(Board board, int maxDeep, int maxChance, boolean writeLog){
        Minimax mnm = new Minimax();
        return mnm.pureMinimaxDecision(board, this, maxDeep, maxChance, writeLog);
    }

    /** Minimax with random selection **/
    public Actions autoAction4(Board board, int maxDeep, int maxChance, boolean writeLog){
        Minimax mnm = new Minimax();
        return mnm.randomMinimaxDecision(board, this, maxDeep, maxChance, writeLog);
    }

    /** Minimax improvement (More child nodes for the first level) **/
    public Actions autoAction5(Board board, int maxDeep, int maxChance, boolean writeLog){
        Minimax mnm = new Minimax();
        return mnm.minimaxDecision2(board, this, maxDeep, maxChance, writeLog);
    }
}
