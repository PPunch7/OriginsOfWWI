package com.example.originsofworldwari;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class Game {
    private static Game instance;
    private Board board;
    private Players[] players;
    private Cards[] cards;
    private String mode = "";
    private Players winner;
    private boolean gameFinish;
    private Players[] ranking;
    private String lastAction = "";
    private ArrayList<HashMap<String, Integer>> history;

    private Game(){
        this.board = Board.getInstance();
        clearPlayers();
        this.cards = new Cards[board.getNumPlayers()];
        this.gameFinish = false;
    }

    public static Game getInstance(){
        if (instance == null)
            instance = new Game();
        return instance;
    }

    public void startGame(){
        restartGame();
        this.players[0].setTotalTurn();
        this.board.setCurrentTurn(players[0]);
        this.board.setCurrentAction("PlacePF");
        this.board.setCurrentAvailablePF(players[0].getCardSelected().getNumPF());
    }

    public void restartGame(){
        this.board.clearBoard();
        this.gameFinish = false;
        for (Players p : this.players) {
            p.clearTotalTurn();
            p.setTotalScore(0);
        }
    }

    public void clearGame(){
        //restartGame();
        clearPlayers();
        this.mode = "";
    }

    private void clearPlayers(){
        this.players = new Players[board.getNumPlayers()];
    }

    public void addPlayer(Players player){
        int index = -1;
        for (int i=this.players.length-1; i>=0; i--){
            if (this.players[i] != null){
                index = i;
                break;
            }
        }
        if (index < this.players.length - 1)
            this.players[index + 1] = player;
    }

    public Players getPlayer(int index){
        return this.players[index];
    }

    public Players[] getAllPlayer() { return this.players; }

    public void addCard(Cards card){
        int index = -1;
        for (int i=this.cards.length-1; i>=0; i--){
            if (this.cards[i] != null){
                index = i;
                break;
            }
        }
        if (index < this.cards.length - 1)
            this.cards[index + 1] = card;
    }

    public Cards getCard(int index){
        return this.cards[index];
    }

    public void setMode(String mode){
        this.mode = mode;
    }

    public String getMode(){
        return this.mode;
    }

    public void setGameFinish(){
        this.gameFinish = true;
    }

    public boolean getGameFinish(){ return this.gameFinish; }

    private void setWinner(Players player){
        this.winner = player;
    }

    public Players getWinner(){
        return this.winner;
    }

    public void placePF(String location, Players player, int pf){
        Nations[] currentBoard = this.board.getBoard();
        for (Nations n : currentBoard){
            if (n.getName().equals(location)){
                n.setPF(player, pf);
                break;
            }
        }

        this.board.setBoard(currentBoard);
        setTreatyRight(location);
    }

    public void calAttack(Nations location, Players attacker, Players defender/*int pfAtt, int pfDef, int numDice*/){
        int pfAtt, pfDef, numDice;
        float ratio;
        String attType;
        numDice = attacker.tollDice();
        pfAtt = location.getPF(attacker);
        pfDef = location.getPF(defender);
        ratio = pfAtt/pfDef;
        attType = checkOdds(ratio, numDice);

        this.board.setCurrentAttPF(pfAtt);
        this.board.setCurrentDefPF(pfDef);
        this.board.setCurrentNumDice(numDice);
        this.board.setCurrentAttType(attType);

        this.board.setCurrentDef(defender);

        removePF(location, attacker, defender, attType);
        removeTreatyRight(location);
    }

    public String checkOdds(float ratio_input, int numDice_input){
        String odds = "";
        if (ratio_input >= 4){
            if (numDice_input >= 4) { odds = "DE"; }
            else { odds = "EX"; }
        }
        else if (ratio_input >= 3){
            if (numDice_input >= 5) { odds = "DE"; }
            else { odds = "EX"; }
        }
        else if (ratio_input >= 2){
            if (numDice_input >= 6) { odds = "DE"; }
            else if (numDice_input >= 2) { odds = "EX"; }
            else { odds = "AE"; }
        }
        else if (ratio_input >= 1){
            if (numDice_input >= 6) { odds = "DE"; }
            else if (numDice_input >= 3) { odds = "EX"; }
            else { odds = "AE"; }
        }
        else {
            if (numDice_input >= 4) { odds = "EX"; }
            else { odds = "AE"; }
        }

        return odds;
    }

    public void removePF(Nations location, Players att, Players def, String attType){
        if (attType.equals("AE")){
            location.setPF(att, location.getPF(att) * -1);
        }
        else if (attType.equals("EX")){
            if (location.getPF(att) > location.getPF(def)) {
                location.setPF(att, location.getPF(def) * -1);
                location.setPF(def, location.getPF(def) * -1);
            }
            else {
                location.setPF(def, location.getPF(att) * -1);
                location.setPF(att, location.getPF(att) * -1);
            }
        }
        else{
            location.setPF(def, location.getPF(def) * -1);
        }
    }

    private void setTreatyRight(String location/*Nations location, Players player*/){
        Players currentPlayer = this.board.getCurrentTurn();
        for (Nations n : this.board.getBoard()){
            if (n.getName().equals(location)) {
                if (n.getPF(currentPlayer) >= 10){
                    n.addTreatyRight(currentPlayer);
                }
                break;
            }
        }
    }

    private void removeTreatyRight(Nations location){
        Players currentAttacker = this.board.getCurrentTurn();
        Players currentDefender = this.board.getCurrentDef();
        if (location.getPF(currentAttacker) < 10)
            location.removeTreatyRight(currentAttacker);
        if (location.getPF(currentDefender) < 10)
            location.removeTreatyRight(currentDefender);
    }

    public void nextTurn(){
        int index = (this.board.getCurrentTurn().getCardSelected().getId() + 1) % this.players.length; // next turn (order by card selected)
        for (Players p : this.players){
            if (p.getCardSelected().getId() == index){
                p.setTotalTurn();
                this.board.setCurrentTurn(p);
                this.board.setCurrentAction("PlacePF");
                this.board.setCurrentAvailablePF(p.getCardSelected().getNumPF());
            }
        }
    }

    public void calTotalScore(){
        Score score = Score.getInstance();
        for (Players p : this.players)
            p.setTotalScore(score.getScore(this.board.getBoard(), p));
        calExtraScoreForUK();

        sortRank();
        setWinner(this.ranking[0]);
    }

    private void calExtraScoreForUK(){
        boolean scoreLess = true;
        Players britain = null;
        for (Players p : this.players){
            if (!(p.getCardSelected().getName().equals("Britain"))){
                if (p.getTotalScore() > 12)
                    scoreLess = false;
            }
            else
                britain = p;
        }

        if (scoreLess){
            int britainScore = britain.getTotalScore();
            britain.setTotalScore(britainScore + 10);
        }
    }

    private void sortRank(){
        this.ranking = this.players.clone();
        Arrays.sort(this.ranking, new Comparator<Players>() {
            @Override
            public int compare(Players o1, Players o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public Players[] getTotalRank(){
        return this.ranking;
    }

    public String getLastAction() { return this.lastAction; }

    public void setLastAction(String action) { this.lastAction = action; }

    public ArrayList<HashMap<String, Integer>> getHistoryTurns() { return this.history; }

    public void setHistoryTurns(ArrayList<HashMap<String, Integer>> his) { this.history = his; }

    public void clearHistoryTurns() { this.history = new ArrayList<HashMap<String, Integer>>(); }

    public void sentHotSeat(Object receiver) {

    }

    public void receiveHotSeat(Object sender){

    }
}
