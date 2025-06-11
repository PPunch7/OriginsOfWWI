package com.example.originsofworldwari;

import android.util.Log;

import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class AITest extends TestCase {

    private Game game = Game.getInstance();
    private Board board = Board.getInstance();
    private LogFile file;// = LogFile.getInstance();

    public void testing(){
        Actions actions;

        game.addCard(createCard(0, "Britain", 14));
//        file.WriteFile("Card added: Britain");
        game.addCard(createCard(1, "France", 12));
//        file.WriteFile("Card added: France");
        game.addCard(createCard(2, "Germany", 16));
//        file.WriteFile("Card added: Germany");
        game.addCard(createCard(3, "Russia", 10));
//        file.WriteFile("Card added: Russia");
        game.addCard(createCard(4, "Austria", 10));
//        file.WriteFile("Card added: Austria-Hungary");

        game.addPlayer(createPlayer(0, "Player01", "green", true, game.getCard(0), "Medium"));
//        file.WriteFile("Player created: Player01 -> Britain");
        game.addPlayer(createPlayer(1, "Player02", "blue", true, game.getCard(1), "Medium"));
//        file.WriteFile("Player created: Player02 -> France");
        game.addPlayer(createPlayer(2, "Player03", "orange", true, game.getCard(2), "Medium"));
//        file.WriteFile("Player created: Player03 -> Germany");
        game.addPlayer(createPlayer(3, "Player04", "pink", true, game.getCard(3), "Medium"));
//        file.WriteFile("Player created: Player04 -> Russia");
        game.addPlayer(createPlayer(4, "Player05", "black", true, game.getCard(4), "Test"));
//        file.WriteFile("Player created: Player05 -> Austria-Hungary");

        // set AI level
        ArrayList<String> levels = new ArrayList<String>();
//        levels.add("Simple");
//        levels.add("Random1");
//        levels.add("Random2");
//        levels.add("Random3");
//        levels.add("Evaluate1");
//        levels.add("Evaluate2");
        levels.add("Evaluate3");
        levels.add("NewEvaluate3");

        Nations britain, france, germany, russia, austria, italy, serbia, romania, bulgaria, greece, turkey, farEast, africa;
        britain = board.getNations(0);
        france = board.getNations(1);
        germany = board.getNations(2);
        russia = board.getNations(3);
        austria = board.getNations(4);
        italy = board.getNations(5);
        serbia = board.getNations(6);
        romania = board.getNations(7);
        bulgaria = board.getNations(8);
        greece = board.getNations(9);
        turkey = board.getNations(10);
        farEast = board.getNations(11);
        africa = board.getNations(12);

        Players[] players = game.getAllPlayer();
        Random r = new Random();
        int levelID;

        for (int nation=1; nation<=5; nation++) {

            for (int pattern = 1; pattern <= 1; pattern++) {

                for (int i=0; i<players.length; i++){
                    if (i == (nation - 1))
                        levelID = levels.size() - 1;
                    else
                        levelID = r.nextInt(levels.size() - 1);

                    players[i].setDifficulty(levels.get(levelID));
                }

                for (int round = 1; round <= 7; round++) {
                    file = new LogFile("N" + nation + "/Pattern" + Integer.toString(pattern) + "/game" + Integer.toString(round) + ".log");

                    file.ClearFile();

                    board.clearBoard();

                    writePlayers();

                    file.WriteFile("--------------------------------------------------------------------");

                    writeCurrentBoard(board.getBoard());

                    file.WriteFile("--------------------------------------------------------------------");

                    file.WriteFile("##### Game start #####");

                    Players currentPlayer;
                    Actions currentAction;
                    game.startGame();

                    while (!(game.getGameFinish())) {
                        currentPlayer = board.getCurrentTurn();

                        if (currentPlayer.getDifficulty().equals("Very Easy"))
                            currentAction = currentPlayer.autoAction1(board);
                        else if (currentPlayer.getDifficulty().equals("Easy"))           //  Easy level
                            currentAction = currentPlayer.autoAction2(board, 1, 11, false);
//                    currentAction = currentPlayer.autoAction1(board);
                        else if (currentPlayer.getDifficulty().equals("Medium"))    //  Medium level
                            currentAction = currentPlayer.autoAction2(board, 6, 5, false);
                        else if (currentPlayer.getDifficulty().equals("Test"))      //  Test level
                            currentAction = currentPlayer.autoAction2(board, 11, 2, false);
                        else if (currentPlayer.getDifficulty().equals("Simple"))
                            currentAction = currentPlayer.autoAction1(board);
                        else if (currentPlayer.getDifficulty().equals("Random1"))
                            currentAction = currentPlayer.autoAction4(board, 8, 5, false);
                        else if (currentPlayer.getDifficulty().equals("Random2"))
                            currentAction = currentPlayer.autoAction4(board, 9, 4, false);
                        else if (currentPlayer.getDifficulty().equals("Random3"))
                            currentAction = currentPlayer.autoAction4(board, 11, 3, false);
                        else if (currentPlayer.getDifficulty().equals("Evaluate1"))
                            currentAction = currentPlayer.autoAction2(board, 6, 5, false);
                        else if (currentPlayer.getDifficulty().equals("Evaluate2"))
                            currentAction = currentPlayer.autoAction2(board, 9, 3, false);
                        else if (currentPlayer.getDifficulty().equals("Evaluate3"))
                            currentAction = currentPlayer.autoAction2(board, 11, 2, false);
                        else if (currentPlayer.getDifficulty().equals("NewEvaluate3"))
                            currentAction = currentPlayer.autoAction5(board, 11, 2, false);
                        else                                                        //  Hard level
                            currentAction = currentPlayer.autoAction2(board, 11, 3, false);

                        file.WriteFile("---------- " + currentPlayer.getCardSelected().getName() + ", Turn: " + currentPlayer.getTotalTurn() + " ----------");

                        // AI placing PF
                        for (Map.Entry<Nations, Integer> entry : currentAction.getPlacePF().entrySet()) {
                            currentPlayer.placePF(entry.getKey().getName(), entry.getValue());
                            file.WriteFile("Placing PF -> Location: " + entry.getKey().getName() + ", Num of PF: " + Integer.toString(entry.getValue()));
//                file.WriteFile("Num of PF: " + Integer.toString(entry.getValue()));
                        }

                        // AI attacking
                        if (currentAction.getAttack().size() > 0) {
                            for (Map.Entry<Nations, Players> entry : currentAction.getAttack().entrySet()) {
                                // find the location
                                Nations location = null;
                                for (Nations n : board.getBoard()) {
                                    if (n.getName().equals(entry.getKey().getName()))
                                        location = n;
                                }

                                // set Defender
                                for (int i=0; i<this.board.getNumPlayers(); i++){
                                    if (this.game.getPlayer(i).getCardSelected().getName().equals(entry.getValue().getCardSelected().getName())){
                                        this.board.setCurrentDef(this.game.getPlayer(i));
                                        break;
                                    }
                                }

//                            p.attack(entry.getKey(), entry.getValue());
                                currentPlayer.attack(location, entry.getValue());
                                file.WriteFile("Attacking -> Location: " + entry.getKey().getName());
                                file.WriteFile("\tDefender: " + entry.getValue().getCardSelected().getName());
                                file.WriteFile("\tAttacker's PF before: " + Integer.toString(board.getCurrentAttPF()) + ", PF after: " + location.getPF(currentPlayer));
                                file.WriteFile("\tDefender's PF before: " + Integer.toString(board.getCurrentDefPF()) + ", PF after: " + location.getPF(entry.getValue()));
                                file.WriteFile("\tDice: " + Integer.toString(board.getCurrentNumDice()));
                                file.WriteFile("\tResult: " + board.getCurrentAttType());
                            }
                        }

                        setGameFinish();
                        checkGameFinish();
                    }

                    file.WriteFile("##### Game finish #####");

                    Players[] ranking = Game.getInstance().getTotalRank();
                    for (int i = 0; i < ranking.length; i++) {
                        file.WriteFile("Rank: " + (i + 1) + ", Nation: " + ranking[i].getCardSelected().getName() + ", VP: " + ranking[i].getTotalScore());
                    }

                    file.WriteFile("--------------------------------------------------------------------");

                    writeCurrentBoard(board.getBoard());
                }
            }
        }
    }

    private void setGameFinish() {
        if (game.getPlayer(board.getNumPlayers() - 1).getTotalTurn() >= 10) {    // check total turn of the last player
            game.setGameFinish();
        }
    }

    private void checkGameFinish(){
        if (game.getGameFinish()) {
            game.calTotalScore();

//            Log.i("Game", "End Turn button clicked");
//        startActivity(new Intent(GameActivity.this, EndGameActivity.class));
        }
        else{
            game.nextTurn();
        }
    }

    private int getTimeInSec(){
        LocalDateTime date = LocalDateTime.now();
        int seconds = date.toLocalTime().toSecondOfDay();
//        file.WriteFile("Time stamp: " + Integer.toString(seconds));
        return seconds;
    }

    private Cards createCard(int id, String name, int pf){
        return new Cards(id, name, pf);
    }

    private Players createPlayer(int id, String name, String colour, boolean isAI, Cards card, String level){
        Players newPlayer = new Players(id, name, colour, isAI);
        newPlayer.selectCard(card);
        newPlayer.setDifficulty(level);
        return newPlayer;
    }

    private void writeCurrentBoard(Nations[] currentBoard){
        file.WriteFile("*** Current Board ***");
        String msg = "Location\t";
        for (int i=0; i<game.getAllPlayer().length; i++){
            msg += game.getPlayer(i).getName() + "\t";
        }
        file.WriteFile(msg);
        for (Nations n : currentBoard){
            msg = n.getName() + "    \t";
            for (int i=0; i<game.getAllPlayer().length; i++){
                msg += Integer.toString(n.getPF(game.getPlayer(i))) + "\t\t";
            }
            file.WriteFile(msg);
        }
    }

    private void writeActions(Actions actions){
        file.WriteFile("*** Placing PF ***");
        for (Map.Entry<Nations, Integer> entry : actions.getPlacePF().entrySet()){
            file.WriteFile("Location: " + entry.getKey().getName() + ", PF: " + Integer.toString(entry.getValue()));
        }
        file.WriteFile("*** Attacking ***");
        for (Map.Entry<Nations, Players> entry : actions.getAttack().entrySet()){
            file.WriteFile("Location: " + entry.getKey().getName() + ", Def: " + entry.getValue().getName());
        }
        file.WriteFile("Total score: " + actions.getScore());
    }

    private void writePlayers(){
        Players[] players = game.getAllPlayer();
        for (int i=0; i<players.length; i++){
            file.WriteFile("Player created: Player0" + (players[i].getId() + 1) + " -> "
                    + players[i].getCardSelected().getName()  + " -> " + players[i].getDifficulty());
        }
    }
}