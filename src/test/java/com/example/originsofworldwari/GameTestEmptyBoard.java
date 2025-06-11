package com.example.originsofworldwari;

import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.util.Map;

public class GameTestEmptyBoard extends TestCase {

    private Game game = Game.getInstance();
    private Board board = Board.getInstance();
    private LogFile file = LogFile.getInstance();

    public void testing(){
        Actions actions;

        file.ClearFile();

        game.addCard(createCard(0, "Britain", 14));
        file.WriteFile("Card added: Britain");
        game.addCard(createCard(1, "France", 12));
        file.WriteFile("Card added: France");
        game.addCard(createCard(2, "Germany", 16));
        file.WriteFile("Card added: Germany");
        game.addCard(createCard(3, "Russia", 10));
        file.WriteFile("Card added: Russia");
        game.addCard(createCard(4, "Austria", 10));
        file.WriteFile("Card added: Austria-Hungary");

        game.addPlayer(createPlayer(0, "Player01", "green", false, game.getCard(0)));
        file.WriteFile("Player created: Player01 -> Britain");
        game.addPlayer(createPlayer(1, "Player02", "blue", false, game.getCard(1)));
        file.WriteFile("Player created: Player02 -> France");
        game.addPlayer(createPlayer(2, "Player03", "orange", false, game.getCard(2)));
        file.WriteFile("Player created: Player03 -> Germany");
        game.addPlayer(createPlayer(3, "Player04", "pink", false, game.getCard(3)));
        file.WriteFile("Player created: Player04 -> Russia");
        game.addPlayer(createPlayer(4, "Player05", "black", false, game.getCard(4)));
        file.WriteFile("Player created: Player05 -> Austria-Hungary");

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

        file.WriteFile("--------------------------------------------------------------------");

//        game.getPlayer(0).placePF(austria.getName(), 3);
//        game.getPlayer(0).placePF(italy.getName(), 17);
//        game.getPlayer(0).placePF(greece.getName(), 14);
//        game.getPlayer(0).placePF(turkey.getName(), 25);
//        game.getPlayer(0).placePF(farEast.getName(), 10);
//
//        game.getPlayer(1).placePF(britain.getName(), 10);
//        game.getPlayer(1).placePF(russia.getName(), 15);
//        game.getPlayer(1).placePF(italy.getName(), 10);
//        game.getPlayer(1).placePF(africa.getName(), 5);
//
//        game.getPlayer(2).placePF(russia.getName(), 5);
//        game.getPlayer(2).placePF(austria.getName(), 10);
//        game.getPlayer(2).placePF(italy.getName(), 15);
//        game.getPlayer(2).placePF(africa.getName(), 17);
//
//        game.getPlayer(3).placePF(serbia.getName(), 10);
//        game.getPlayer(3).placePF(romania.getName(), 10);
//        game.getPlayer(3).placePF(turkey.getName(), 8);
//        game.getPlayer(3).placePF(farEast.getName(), 9);
//
//        game.getPlayer(4).placePF(serbia.getName(), 10);
//        game.getPlayer(4).placePF(germany.getName(), 10);
//        game.getPlayer(4).placePF(italy.getName(), 1);
//        game.getPlayer(4).placePF(romania.getName(), 6);

        writeCurrentBoard(board.getBoard());

        file.WriteFile("--------------------------------------------------------------------");

//        actions = game.getPlayer(0).minimaxDecision(board);
        int start = getTimeInSec();
//        actions = game.getPlayer(0).autoAction2(board, 6, 3);
//        writeActions(actions);
//        file.WriteFile("Total time spent: " + (getTimeInSec() - start) + " seconds");
//        file.WriteFile("--------------------------------------------------------------------");

        start = getTimeInSec();
        actions = game.getPlayer(0).autoAction2(board, 6, 7, true);
        writeActions(actions);
        file.WriteFile("Total time spent: " + (getTimeInSec() - start) + " seconds");
        file.WriteFile("--------------------------------------------------------------------");

//        start = getTimeInSec();
//        actions = game.getPlayer(0).autoAction2(board, 6, 7);
//        writeActions(actions);
//        file.WriteFile("Total time spent: " + (getTimeInSec() - start) + " seconds");
//        file.WriteFile("--------------------------------------------------------------------");
//
//        start = getTimeInSec();
//        actions = game.getPlayer(0).autoAction2(board, 11, 3);
//        writeActions(actions);
//        file.WriteFile("Total time spent: " + (getTimeInSec() - start) + " seconds");
//        file.WriteFile("--------------------------------------------------------------------");
//
//        start = getTimeInSec();
//        actions = game.getPlayer(0).autoAction2(board, 11, 5);
//        writeActions(actions);
//        file.WriteFile("Total time spent: " + (getTimeInSec() - start) + " seconds");
//        file.WriteFile("--------------------------------------------------------------------");
//
//        start = getTimeInSec();
//        actions = game.getPlayer(0).autoAction2(board, 11, 7);
//        writeActions(actions);
//        file.WriteFile("Total time spent: " + (getTimeInSec() - start) + " seconds");
//        for (Actions a : actions)
//            writeActions(a);

        file.WriteFile("--------------------------------------------------------------------");
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

    private Players createPlayer(int id, String name, String colour, boolean isAI, Cards card){
        Players newPlayer = new Players(id, name, colour, isAI);
        newPlayer.selectCard(card);
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
            msg = n.getName() + "   \t";
            for (int i=0; i<game.getAllPlayer().length; i++){
                msg += Integer.toString(n.getPF(game.getPlayer(i))) + "\t\t";
            }
            file.WriteFile(msg);
        }
    }

    private void writeActions(Actions actions){
        file.WriteFile("########## SELECTED ##########");
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
}