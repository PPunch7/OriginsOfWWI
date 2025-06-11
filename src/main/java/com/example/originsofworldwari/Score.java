package com.example.originsofworldwari;

import java.util.HashMap;

public class Score {
    private static Score instance;
    private HashMap<String, HashMap<Nations, Integer>> scoreList;
    private Board board;

    private Score(){
        this.scoreList = new HashMap<String, HashMap<Nations, Integer>>();
        this.board = Board.getInstance();
        addScoreList();
    }

    public static Score getInstance(){
        if (instance == null)
            instance = new Score();
        return instance;
    }

    private void addScoreList(){
        HashMap<Nations, Integer> bList = new HashMap<Nations, Integer>();
        HashMap<Nations, Integer> fList = new HashMap<Nations, Integer>();
        HashMap<Nations, Integer> gList = new HashMap<Nations, Integer>();
        HashMap<Nations, Integer> rList = new HashMap<Nations, Integer>();
        HashMap<Nations, Integer> aList = new HashMap<Nations, Integer>();
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

        // for Britain
        bList.clear();
        bList.put(italy, 3);
        bList.put(greece, 1);
        bList.put(turkey, 2);
        bList.put(farEast, 4);
        scoreList.put("Britain", bList);

        // for France
        fList.clear();
        fList.put(britain, 2);
        fList.put(russia, 3);
        fList.put(italy, 1);
        fList.put(africa, 5);
        scoreList.put("France", fList);

        // for Germany
        gList.clear();
        gList.put(austria, 4);
        gList.put(russia, 2);
        gList.put(italy, 2);
        gList.put(africa, 3);
        scoreList.put("Germany", gList);

        // for Russia
        rList.clear();
        rList.put(serbia, 5);
        rList.put(romania, 3);
        rList.put(bulgaria, 1);
        rList.put(greece, 1);
        rList.put(farEast, 3);
        rList.put(turkey, 5);
        scoreList.put("Russia", rList);

        // for Austria-Hungary
        aList.clear();
        aList.put(germany, 4);
        aList.put(italy, 2);
        aList.put(romania, 2);
        aList.put(serbia, 10);
        scoreList.put("Austria", aList);
    }

    public HashMap<Nations, Integer> getScoreList(String nation) { return this.scoreList.get(nation); }

    public int getScore(Nations[] currentBoard, Players player){
        String nation = player.getCardSelected().getName();
        int score = 0;

        switch (nation) {
            case "Britain":
                if (checkTreatyRight(currentBoard, player, "Italy"))
                    score += 3;
                if (checkTreatyRight(currentBoard, player, "Greece"))
                    score += 1;
                if (checkTreatyRight(currentBoard, player, "Turkey"))
                    score += 2;
                if (checkTreatyRightExclusive(currentBoard, player, "Far East"))
                    score += 4;
                break;
            case "France":
                if (checkTreatyRight(currentBoard, player, "Britain"))
                    score += 2;
                if (checkTreatyRight(currentBoard, player, "Russia"))
                    score += 3;
                if (checkTreatyRight(currentBoard, player, "Italy"))
                    score += 1;
                if (checkTreatyRightExclusive(currentBoard, player, "Africa"))
                    score += 5;
                if (checkNoTreatyRight(currentBoard, "Germany"))
                    score += 10;
                break;
            case "Germany":
                if (checkTreatyRight(currentBoard, player, "Austria"))
                    score += 4;
                if (checkTreatyRight(currentBoard, player, "Russia"))
                    score += 2;
                if (checkTreatyRight(currentBoard, player, "Italy"))
                    score += 2;
                if (checkTreatyRight(currentBoard, player, "Africa"))
                    score += 3;
                if (checkNoTreatyRight(currentBoard, "Britain"))
                    score += 5;
                break;
            case "Russia":
                if (checkTreatyRight(currentBoard, player, "Serbia"))
                    score += 5;
                if (checkTreatyRight(currentBoard, player, "Romania"))
                    score += 3;
                if (checkTreatyRight(currentBoard, player, "Bulgaria"))
                    score += 1;
                if (checkTreatyRight(currentBoard, player, "Greece"))
                    score += 1;
                if (checkTreatyRight(currentBoard, player, "Far East"))
                    score += 3;
                if (checkTreatyRightExclusive(currentBoard, player, "Turkey"))
                    score += 5;
                break;
            case "Austria":
                if (checkTreatyRight(currentBoard, player, "Germany"))
                    score += 4;
                if (checkTreatyRight(currentBoard, player, "Italy"))
                    score += 2;
                if (checkTreatyRight(currentBoard, player, "Romania"))
                    score += 2;
                if (checkTreatyRightExclusive(currentBoard, player, "Serbia"))
                    score += 10;
                break;
        }

        return score;
    }

    private boolean checkTreatyRight(Nations[] currentBoard, Players player, String nation){
        boolean foundTreatyRight = false;

        for (Nations n : currentBoard){
            if (n.getName().equals(nation)){
                if (n.getTreatyRight().contains(player))
                    foundTreatyRight = true;
                break;
            }
        }
        return foundTreatyRight;
    }

    private boolean checkTreatyRightExclusive(Nations[] currentBoard, Players player, String nation){
        boolean foundTreatyRight = false, onlyNation = false, foundTreatyRightEx = false;

        for (Nations n : currentBoard){
            if (n.getName().equals(nation)){
                if (n.getTreatyRight().contains(player))
                    foundTreatyRight = true;
                if (n.getTreatyRight().size() == 1)
                    onlyNation = true;
                break;
            }
        }

        if (foundTreatyRight && onlyNation)
            foundTreatyRightEx = true;

        return foundTreatyRightEx;
    }

    private boolean checkNoTreatyRight(Nations[] currentBoard, String nation){
        boolean noTreatyRight = true;
        Players player = null;

        // find player who represents the nation
        for (Players p : Game.getInstance().getAllPlayer()){
            if (p.getCardSelected().getName().equals(nation))
                player = p;
        }

        // check the player no give or receive any treaty right from each other
        for (Nations n : currentBoard){
            // check other nation
            if (!(n.getName().equals(nation))){
                if (n.getTreatyRight().contains(player)) {
                    noTreatyRight = false;
                    break;
                }
            }
            // check the nation that is the same as the player
            else{
                if (n.getTreatyRight().size() > 1) {
                    noTreatyRight = false;
                    break;
                }
                else if (n.getTreatyRight().size() == 1){
                    if (!(n.getTreatyRight().contains(player))){
                        noTreatyRight = false;
                        break;
                    }
                }
            }
        }
        return noTreatyRight;
    }
}
