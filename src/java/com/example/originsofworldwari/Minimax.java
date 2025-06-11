package com.example.originsofworldwari;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Minimax {

    public Actions simpleDecision(Board board, Players player){
        Random r = new Random();
        Actions actions = new Actions(player);
        ArrayList<Nations> currentBoard = new ArrayList<Nations>(Arrays.asList(board.getBoard()));
        int availablePF = player.getCardSelected().getNumPF();
        HashMap<Nations, Integer> scoreList = Score.getInstance().getScoreList(player.getCardSelected().getName());
        ArrayList<HashMap<Nations, Integer>> opportunity = new ArrayList<HashMap<Nations, Integer>>();
        ArrayList<Integer> opportunityScore = new ArrayList<Integer>();
        HashMap<Nations, Integer> newOpportunity;
        int currentPF, currentScore, roundScoreList = 0;
        HashMap<Nations, Integer> bestOpportunity = new HashMap<Nations, Integer>();
        int placePF = 0;
        Nations location = null;

        /** Place PF **/
        while (availablePF > 0){
            if (roundScoreList < scoreList.size()) {    // if go through around scoreList
                // select max value from scoreList selection
                opportunity = new ArrayList<HashMap<Nations, Integer>>();
                opportunityScore = new ArrayList<Integer>();
                for (int pf = 1; pf <= availablePF && pf <= 5; pf++) {
                    for (Map.Entry<Nations, Integer> entry : scoreList.entrySet()) {
                        newOpportunity = new HashMap<Nations, Integer>();
                        currentScore = 0;
                        if (currentBoard.contains(entry.getKey())) {
                            currentPF = entry.getKey().getPF(player);
                            newOpportunity.put(entry.getKey(), pf);
                            opportunity.add(newOpportunity);
                            currentScore += scorePlacePFtoGetTP(entry.getValue(), currentPF + pf);
                            currentScore += scorePlacePFtoProtect(entry.getKey(), player, currentPF + pf);
                            opportunityScore.add(currentScore);
                        }
                    }
                }
                bestOpportunity = opportunity.get(opportunityScore.indexOf(Collections.max(opportunityScore)));
                currentScore = actions.getScore() + Collections.max(opportunityScore);
                actions.setScore(currentScore);
                for (Map.Entry<Nations, Integer> entry : bestOpportunity.entrySet()) {
                    location = entry.getKey();
                    placePF = entry.getValue();
                }

                roundScoreList ++;
            }
            else {
                // placing PF to prevent
                Players currentPrevent = getPrevent(player);
                if (player.getCardSelected().getName().equals("France")){
                    for (int pf = 1; pf <= availablePF && pf <= 5; pf++) {
                        newOpportunity = new HashMap<Nations, Integer>();
                        currentScore = 0;
                        for (Nations n : currentBoard){
                            if (currentPrevent.getCardSelected().getName().equals("Germany")){
                                for (Players p : Game.getInstance().getAllPlayer()){
                                    if (!(p.equals(player)))
                                        currentScore += scorePlacePFtoPrevent(n, p);
                                }
                            }
                            else {
                                currentScore += scorePlacePFtoPrevent(n, currentPrevent);
                            }
                            newOpportunity.put(n, pf);
                            opportunity.add(newOpportunity);
                            opportunityScore.add(currentScore);
                        }
                    }
                    bestOpportunity = opportunity.get(opportunityScore.indexOf(Collections.max(opportunityScore)));
                    currentScore = actions.getScore() + Collections.max(opportunityScore);
                    actions.setScore(currentScore);
                    for (Map.Entry<Nations, Integer> entry : bestOpportunity.entrySet()) {
                        location = entry.getKey();
                        placePF = entry.getValue();
                    }
                }
                else if (player.getCardSelected().getName().equals("Germany")){
                    for (int pf = 1; pf <= availablePF && pf <= 5; pf++) {
                        newOpportunity = new HashMap<Nations, Integer>();
                        currentScore = 0;
                        for (Nations n : currentBoard){
                            if (currentPrevent.getCardSelected().getName().equals("Britain")){
                                for (Players p : Game.getInstance().getAllPlayer()){
                                    if (!(p.equals(player)))
                                        currentScore += scorePlacePFtoPrevent(n, p);
                                }
                            }
                            else {
                                currentScore += scorePlacePFtoPrevent(n, currentPrevent);
                            }
                            newOpportunity.put(n, pf);
                            opportunity.add(newOpportunity);
                            opportunityScore.add(currentScore);
                        }
                    }
                    bestOpportunity = opportunity.get(opportunityScore.indexOf(Collections.max(opportunityScore)));
                    currentScore = actions.getScore() + Collections.max(opportunityScore);
                    actions.setScore(currentScore);
                    for (Map.Entry<Nations, Integer> entry : bestOpportunity.entrySet()) {
                        location = entry.getKey();
                        placePF = entry.getValue();
                    }
                }

                else {
                    // just random the location
                    if (availablePF > 5)
                        placePF = r.nextInt(5) + 1;
                    else
                        placePF = r.nextInt(availablePF) + 1;
                    location = currentBoard.get(r.nextInt(currentBoard.size()));
                }
            }

            currentBoard.remove(location);
            actions.setPlace(location, placePF);
            availablePF -= placePF;
        }

        /** Attack **/
        Nations[] boardTemp = updateBoardTemp(board.getBoard(), player, actions.getPlacePF());
        HashMap<Nations, Players> newAttOpportunity = new HashMap<Nations, Players>();
        ArrayList<HashMap<Nations, Players>> attOpportunity = new ArrayList<HashMap<Nations, Players>>();
        ArrayList<Integer> attOpportunityScore = new ArrayList<Integer>();
        HashMap<Nations, Players> bestAttOpportunity = new HashMap<Nations, Players>();
        Nations attLocation;
        Players defender;

        int attPF, defPF;
        for (Map.Entry<Nations, Integer> entry : scoreList.entrySet()) {
            newAttOpportunity = new HashMap<Nations, Players>();
            currentScore = 0;
            for (Nations n : boardTemp){
                if (n.getName().equals(entry.getKey().getName())){
                    attPF = n.getPF(player);
                    for (Players p : Game.getInstance().getAllPlayer()){
                        if (!(p.equals(player))){
                            defPF = n.getPF(p);
                            currentScore = scoreAttack(attPF, defPF, entry.getValue());
                            if (currentScore > 0){
                                newAttOpportunity.put(entry.getKey(), p);
                                attOpportunity.add(newAttOpportunity);
                                attOpportunityScore.add(currentScore);
                            }
                        }
                    }
                }
            }
        }
        if (attOpportunity.size() > 0){
            bestAttOpportunity = attOpportunity.get(attOpportunityScore.indexOf(Collections.max(attOpportunityScore)));
            currentScore = actions.getScore() + Collections.max(attOpportunityScore);
            actions.setScore(currentScore);
            for (Map.Entry<Nations, Players> entry : bestAttOpportunity.entrySet()) {
                attLocation = entry.getKey();
                defender = entry.getValue();
                actions.setAttack(attLocation, defender);
            }
        }
        boardTemp = clearBoard(boardTemp);

        return actions;
    }

    private int scorePlacePFtoGetTP(int score, int currentPF){
        if (currentPF > 15)
            return 0;
        if (currentPF > 10)
            return 10 * score;
        else
            return currentPF * score;
    }

    private int scorePlacePFtoProtect(Nations location, Players player, int currentPF){
        int score = 0;
        for (Map.Entry<Players, Integer> entry : location.getAllPF().entrySet()){
            if (!(entry.getKey().equals(player)))
                score += currentPF - entry.getValue();
        }

        return score;
    }

    private int scorePlacePFtoPrevent(Nations location, Players player){
        int score = 0;
        int pf = location.getPF(player);
        if (pf > 5)
            score = pf;

        return  score;
    }

    private Players getPrevent(Players player){
        Players toPrevent = null;
        for (Players p : Game.getInstance().getAllPlayer()){
            if (player.getCardSelected().getName().equals("France")){
                if (p.getCardSelected().getName().equals("Germany"))
                    toPrevent = p;
            }
            else if (player.getCardSelected().getName().equals("Germany")){
                if (p.getCardSelected().getName().equals("Britain"))
                    toPrevent = p;
            }
        }
        return  toPrevent;
    }

    private Nations[] updateBoardTempAttack(Nations[] currentBoard, Players attacker, HashMap<Nations, Players> currentAttack, int numDice){
        int attPF = 0, defPF = 0;
        Nations location = null;
        Players defender = null;

        // get location and defender
        for (Map.Entry<Nations, Players> entry : currentAttack.entrySet()){
            location = entry.getKey();
            defender = entry.getValue();
        }

        // get numbers of PF
        for (Nations n : currentBoard){
            if (n.getName().equals(location.getName())){
                location = n;
                attPF = n.getPF(attacker);
                defPF = n.getPF(defender);
                break;
            }
        }

        // get result of attacking
        if (defPF != 0) {
            String resultCode = Game.getInstance().checkOdds((attPF / defPF), numDice);
            Game.getInstance().removePF(location, attacker, defender, resultCode);
        }

        return currentBoard;
    }

    private Nations[] updateBoardTemp(Nations[] currentBoard, Players player, HashMap<Nations, Integer> currentPlacePF){
        Nations[] boardTemp = new Nations[currentBoard.length];
        for (int i=0; i<boardTemp.length; i++)
            boardTemp[i] = cloneNations(currentBoard[i]);
        for (Map.Entry<Nations, Integer> entry : currentPlacePF.entrySet())
            boardTemp[entry.getKey().getId()].setPF(player, entry.getValue());

        return boardTemp;
    }

    private Nations cloneNations(Nations original){
        Nations newNation = new Nations(original.getId(), original.getName(), original.getCanPlay());
        ArrayList<Players> treatyRight = original.getTreatyRight();
        for (Players p : Game.getInstance().getAllPlayer())
            newNation.setPF(p, original.getPF(p));
        if (treatyRight.size() > 0){
            for (Players p : treatyRight)
                newNation.addTreatyRight(p);
        }

        return newNation;
    }

    private Nations[] clearBoard(Nations[] currentBoard){
        for (Nations n : currentBoard)
            n = null;
        return  currentBoard;
    }

    private int scoreAttack(int currentPF, int defPF, int score){
        if (defPF <= 0)
            return -999;
        else{
            if ((currentPF/defPF) >= 4)
                return 8 * score;
            else if ((currentPF/defPF) >= 3)
                return 4 * score;
            else if ((currentPF/defPF) >= 2)
                return 2 * score;
            else if ((currentPF/defPF) >= 1)
                return score;
            else
                return (-2) * score;
        }
    }

    private int scorePlacingTooMuch(Nations location, Players player){
        int score = 0;
        int numLessPF = 0;
        int ownPF = location.getPF(player);
        int otherPF;

        if (ownPF > 25){
            for (Players p : Game.getInstance().getAllPlayer()){
                if (!(p.equals(player))){
                    otherPF = location.getPF(p);
                    if (otherPF != 0) {
                        if ((ownPF / otherPF) > 4)
                            numLessPF++;
                    }
                }
            }
            if (numLessPF == 4)     // all others have a few PF
                score = -100;
        }
        else if (ownPF > 15){
            for (Players p : Game.getInstance().getAllPlayer()){
                if (!(p.equals(player))){
                    otherPF = location.getPF(p);
                    if (otherPF == 0)
                        numLessPF ++;
                }
            }
            if (numLessPF == 4)     // all others have 0 PF
                score = -100;
        }

        return score;
    }

    private int scoreExclusiveTP(Nations[] currentBoard, Players player){
        int score = 0;
        Nations exclusiveLocation = null;
        boolean noEachTP = true;
        boolean getTP = false;

        if (player.getCardSelected().getName().equals("Britain")){
            exclusiveLocation = currentBoard[11];   // Far East
        }
        else if (player.getCardSelected().getName().equals("France")){
            exclusiveLocation = currentBoard[12];   // Africa
        }
        else if (player.getCardSelected().getName().equals("Russia")){
            exclusiveLocation = currentBoard[10];   // Turkey
        }
        else if (player.getCardSelected().getName().equals("Austria")){
            exclusiveLocation = currentBoard[6];    // Serbia
        }

        if (exclusiveLocation != null){
            // current player got TP at EXCLUSIVE location
            if (exclusiveLocation.getPF(player) >= 10){
                getTP = true;
                for (Players p : Game.getInstance().getAllPlayer()){
                    // check other players
                    if (!(p.getCardSelected().getName().equals(player.getCardSelected().getName()))){
                        if (exclusiveLocation.getPF(p) >= 5) {
                            noEachTP = false;
                            break;
                        }
                    }
                }
            }

            // only current player got TP at EXCLUSIVE location
            if (getTP && noEachTP) {
                if (player.getCardSelected().getName().equals("Britain")) {
                    score += 40;
                } else if (player.getCardSelected().getName().equals("France")) {
                    score += 50;
                } else if (player.getCardSelected().getName().equals("Russia")) {
                    score += 50;
                } else if (player.getCardSelected().getName().equals("Austria")) {
                    score += 100;
                }
            }
        }

        return score * 2;
    }

    @SuppressLint("NewApi")
    public Actions minimaxDecision(Board board, final Players player, int maxDeep, int maxChance, boolean writeLog){
        Actions actionSelected;
        ArrayList<Actions> allActions = createOpportunity(board.getBoard(), player, maxChance, 0);

        // create temp board with the new actions
        final ArrayList<Nations[]> tmpBoardList = new ArrayList<Nations[]>();
        final ArrayList<Nations[]> tmpBoardListScore = new ArrayList<Nations[]>();

        // ab-pruning condition
        int[] alphaScore = new int[5];
        alphaScore[0] = -1; // initial score

        for (int i=0; i<allActions.size() ; i++){
            Actions tmpAction = allActions.get(i);
            Nations[] tmpBoard = updateBoardTemp(board.getBoard(), player, tmpAction.getPlacePF());
            if (tmpAction.getAttack().size() > 0){
                int numDice;
//                if ((deepLvl - 1) % 5 == 0)
                    numDice = 4;                // the worst case for current player
//                else
//                    numDice = 6;                // the best case for other players
                tmpBoard = updateBoardTempAttack(tmpBoard, player, tmpAction.getAttack(), numDice);
            }

            tmpBoardList.add(tmpBoard);

            /** Write to file **/
            LogFile file = LogFile.getInstance();
            if (writeLog) {
                file.WriteFile("********** Player: " + player.getCardSelected().getName() + " -> Level: " + 0 + " **********");
                writeActions(tmpAction, "");
                file.WriteFile("******************************************");

                writeCurrentBoard(tmpBoard, player, 0, "");
            }

            // minimax last board
            tmpBoardListScore.add(minimaxLastBoard(tmpBoardList.get(i), player, maxDeep, 1, maxChance, alphaScore, writeLog));
        }

        // parallel minimax
//        tmpBoardList.parallelStream().forEach(
//                new Consumer<Nations[]>() {
//                    @Override
//                    public void accept(Nations[] s) {
//                        int index = tmpBoardList.indexOf(s);
//                        tmpBoardListScore.add(minimaxLastBoard(tmpBoardList.get(index), player, maxDeep, 1));
//                    }
//                }
//        );

        // calculate score and choose
        ArrayList<Integer> scoreList = new ArrayList<Integer>();
        for (int i=0; i<tmpBoardListScore.size(); i++){
            scoreList.add(Score.getInstance().getScore(tmpBoardListScore.get(i), player));
        }

        // add max score to the list
        ArrayList<Integer> indexSelectedList = new ArrayList<Integer>();
        int scoreSelected = Collections.max(scoreList);
        for (int i=0; i<scoreList.size(); i++){
            if (scoreList.get(i).equals(scoreSelected))
                indexSelectedList.add(i);
        }

        // random actionSelected from the list
        Random r = new Random();
        int randomIndex = r.nextInt(indexSelectedList.size());
        actionSelected = allActions.get(indexSelectedList.get(randomIndex));

        /** Write to file **/
        if (writeLog) {
            LogFile file = LogFile.getInstance();
            for (int i = 0; i < tmpBoardListScore.size(); i++) {
                file.WriteFile("## total VP of board " + i + ": " + scoreList.get(i));
            }

            file.WriteFile("### index selected: " + indexSelectedList.get(randomIndex) + " with VP: " + scoreList.get(indexSelectedList.get(randomIndex)));
        }

        return actionSelected;
    }

    @SuppressLint("NewApi")
    public Actions minimaxDecision2(Board board, final Players player, int maxDeep, int maxChance, boolean writeLog){
        Actions actionSelected;
        ArrayList<Actions> allActions = createOpportunity(board.getBoard(), player, maxChance + 1, 0);

        // create temp board with the new actions
        final ArrayList<Nations[]> tmpBoardList = new ArrayList<Nations[]>();
        final ArrayList<Nations[]> tmpBoardListScore = new ArrayList<Nations[]>();

        // ab-pruning condition
        int[] alphaScore = new int[5];
        alphaScore[0] = -1; // initial score

        for (int i=0; i<allActions.size() ; i++){
            Actions tmpAction = allActions.get(i);
            Nations[] tmpBoard = updateBoardTemp(board.getBoard(), player, tmpAction.getPlacePF());
            if (tmpAction.getAttack().size() > 0){
                int numDice;
//                if ((deepLvl - 1) % 5 == 0)
                numDice = 4;                // the worst case for current player
//                else
//                    numDice = 6;                // the best case for other players
                tmpBoard = updateBoardTempAttack(tmpBoard, player, tmpAction.getAttack(), numDice);
            }

            tmpBoardList.add(tmpBoard);

            /** Write to file **/
            LogFile file = LogFile.getInstance();
            if (writeLog) {
                file.WriteFile("********** Player: " + player.getCardSelected().getName() + " -> Level: " + 0 + " **********");
                writeActions(tmpAction, "");
                file.WriteFile("******************************************");

                writeCurrentBoard(tmpBoard, player, 0, "");
            }

            // minimax last board
            tmpBoardListScore.add(minimaxLastBoard(tmpBoardList.get(i), player, maxDeep, 1, maxChance, alphaScore, writeLog));
        }

        // parallel minimax
//        tmpBoardList.parallelStream().forEach(
//                new Consumer<Nations[]>() {
//                    @Override
//                    public void accept(Nations[] s) {
//                        int index = tmpBoardList.indexOf(s);
//                        tmpBoardListScore.add(minimaxLastBoard(tmpBoardList.get(index), player, maxDeep, 1));
//                    }
//                }
//        );

        // calculate score and choose
        ArrayList<Integer> scoreList = new ArrayList<Integer>();
        for (int i=0; i<tmpBoardListScore.size(); i++){
            scoreList.add(Score.getInstance().getScore(tmpBoardListScore.get(i), player));
        }

        // add max score to the list
        ArrayList<Integer> indexSelectedList = new ArrayList<Integer>();
        int scoreSelected = Collections.max(scoreList);
        for (int i=0; i<scoreList.size(); i++){
            if (scoreList.get(i).equals(scoreSelected))
                indexSelectedList.add(i);
        }

        // random actionSelected from the list
        Random r = new Random();
        int randomIndex = r.nextInt(indexSelectedList.size());
        actionSelected = allActions.get(indexSelectedList.get(randomIndex));

        /** Write to file **/
        if (writeLog) {
            LogFile file = LogFile.getInstance();
            for (int i = 0; i < tmpBoardListScore.size(); i++) {
                file.WriteFile("## total VP of board " + i + ": " + scoreList.get(i));
            }

            file.WriteFile("### index selected: " + indexSelectedList.get(randomIndex) + " with VP: " + scoreList.get(indexSelectedList.get(randomIndex)));
        }

        return actionSelected;
    }

    @SuppressLint("NewApi")
    public Nations[] minimaxLastBoard(Nations[] currentBoard, Players player, int maxDeep, int deepLvl, int maxChance, int[] alphaScore, boolean writeLog){

        Players currentTurn = Board.getInstance().getCurrentTurn();
        if ((deepLvl < maxDeep) && (currentTurn.getTotalTurn() + (deepLvl/(5 - currentTurn.getCardSelected().getId()))) <= 10) {
            Players nextTurn = null;
            int cardID = (player.getCardSelected().getId() + 1) % Board.getInstance().getNumPlayers(); // next turn (order by card selected)

            // find the next turn
            for (Players p : Game.getInstance().getAllPlayer()) {
                if (p.getCardSelected().getId() == cardID) {
                    nextTurn = p;
                    break;
                }
            }

            // generate next board
            ArrayList<Actions> allActions = createOpportunity(currentBoard, nextTurn, maxChance, deepLvl);

            // create temp board with the new actions
            ArrayList<Nations[]> tmpBoardList = new ArrayList<Nations[]>();
            ArrayList<Nations[]> tmpBoardListScore = new ArrayList<Nations[]>();

            // ab-pruning condition
            int[] betaScore = new int[5];
            int indexPlayer;
            Nations[] pruningBoard, boardSelected = null;
            boolean selected = true;

            betaScore[0] = -1; // initial score

            for (int i=0; i<allActions.size(); i++) {
                Actions tmpAction = allActions.get(i);
                Nations[] tmpBoard = updateBoardTemp(currentBoard, nextTurn, tmpAction.getPlacePF());
                if (tmpAction.getAttack().size() > 0) {
                    int numDice;
                    if ((deepLvl) % 5 == 0)
                        numDice = 4;                // the worst case for current player
                    else
                        numDice = 6;                // the best case for other players
                    tmpBoard = updateBoardTempAttack(tmpBoard, nextTurn, tmpAction.getAttack(), numDice);
                }

                tmpBoardList.add(tmpBoard);

                /** Write to file **/
                LogFile file = LogFile.getInstance();
                if (writeLog) {
                    String blank = "";
                    for (int j = 0; j < deepLvl; j++)
                        blank += "\t";
                    file.WriteFile(blank + "********** Player: " + nextTurn.getCardSelected().getName() + " -> Level: " + (deepLvl) + " **********");
                    writeActions(tmpAction, blank);
                    file.WriteFile(blank + "******************************************");

                    writeCurrentBoard(tmpBoard, nextTurn, (deepLvl), blank);
                }

                // minimax last board -> going MORE deep or adding TREATY RIGHT
                indexPlayer = nextTurn.getId();
                pruningBoard = minimaxLastBoard(tmpBoardList.get(i), nextTurn, maxDeep, deepLvl + 1, maxChance, betaScore, writeLog);
                if (i == 0) {   // the first node in each level
                    for (int j = 0; j < betaScore.length; j++) {
                        betaScore[j] = Score.getInstance().getScore(pruningBoard, Game.getInstance().getPlayer(j));
                    }
                    boardSelected = pruningBoard;

                    /** Checking pruning **/
                    if (alphaScore[0] != -1) {
                        int indexBefore = indexPlayer - 1;
                        if (indexBefore == -1)
                            indexBefore = 4;
                        if ((betaScore[indexPlayer] > alphaScore[indexPlayer]) || (betaScore[indexBefore] < alphaScore[indexBefore])) {
                            // cut child as pruning
                            break;
                        }
                    }

                }
                else{       // other nodes
                    for (int j=0; j < betaScore.length; j++){
                        int score = Score.getInstance().getScore(pruningBoard, Game.getInstance().getPlayer(j));
                        if (j == indexPlayer){  // the score have to equal or more
                            if (score < betaScore[j]) {
                                selected = false;
                                break;
                            }
                        }
                    }
                    if (selected) {
                        for (int j = 0; j < betaScore.length; j++) {
                            betaScore[j] = Score.getInstance().getScore(pruningBoard, Game.getInstance().getPlayer(j));
                        }
                        boardSelected = pruningBoard;

                        /** Checking pruning **/
                        if (alphaScore[0] != -1) {
                            int indexBefore = indexPlayer - 1;
                            if (indexBefore == -1)
                                indexBefore = 4;
                            if ((betaScore[indexPlayer] > alphaScore[indexPlayer]) || (betaScore[indexBefore] < alphaScore[indexBefore])) {
                                // cut child as pruning
                                break;
                            }
                        }
                    }
                }
            }

            return boardSelected;

        }else {
            // set treaty right
            for (Nations n : currentBoard) {
                for (Players p : Game.getInstance().getAllPlayer()) {
                    if (n.getPF(p) >= 10)
                        n.addTreatyRight(p);
                }
            }

            return currentBoard;
        }
    }

    @SuppressLint("NewApi")
    private ArrayList<Actions> createOpportunity(Nations[] board, Players currentTurn, int maxChance, int deepLvl){
        Players player = currentTurn;
        ArrayList<Actions> actions = new ArrayList<Actions>();

        /***** CreateOpportunity *****/
        /** Place PF **/

        // find the nations which should place
        ArrayList<Nations> locations = findLocationsForPlacing(board, player);
        int[] placePF = new int[locations.size()];

        // get the number of PF
        int availablePF = player.getCardSelected().getNumPF();
        int maxPF = 5;  // the maximum pf for placing per location per turn

        // place PF by order of those nations
        availablePF = player.getCardSelected().getNumPF();
        // for the first location -> first opportunity
        for (int i=0; i<locations.size(); i++){
            if (availablePF > maxPF) {
                placePF[i] = maxPF;
                availablePF -= maxPF;
            }
            else{
                placePF[i] = availablePF;
                availablePF -= availablePF;
            }
        }
        actions.add(addPlacingPF(locations, placePF, player));

        // for the other
        int last = locations.size() - 1;
        distributePFLast(actions, locations, placePF.clone(), player, last);
        distributePFAvg(actions, locations, placePF.clone(), player);
        distributePFMax(actions, locations, placePF.clone(), player, placePF.length - 1, placePF.length, player.getCardSelected().getNumPF(), 0);
        distributePFGroup(actions, locations, placePF.clone(), player);

        /** Attacking **/
        int placePFLength = actions.size();
        for (int i=0; i<placePFLength; i++){
            Nations[] tempBoard = updateBoardTemp(board, player, actions.get(i).getPlacePF());
            addAttacking(actions, actions.get(i), tempBoard, player);
        }

        /** Calculate actions score **/
        for (Actions a : actions)
            calculateActionsScore(board, player, a, deepLvl);

        /** Sort the actions list order by Score : index 0 is the biggest score **/
        Collections.sort(actions);

        /** Select the top score **/
        while (actions.size() > maxChance)
            actions.remove(actions.size() - 1);

        return actions;
    }

    @SuppressLint("NewApi")
    private void writeCurrentBoard(Nations[] currentBoard, Players currentTurn, int deepLvl, String blank){
        LogFile file = LogFile.getInstance();
        file.WriteFile(blank + "--- Temp Board -> Player: " + currentTurn.getCardSelected().getName() + ", Level: " + deepLvl + " -----------------");
        String msg = "Location\t";
        for (int i=0; i<Game.getInstance().getAllPlayer().length; i++){
            msg += Game.getInstance().getPlayer(i).getName() + "\t";
        }
        file.WriteFile(blank + msg);
        for (Nations n : currentBoard){
            msg = n.getName() + "   \t";
            for (int i=0; i<Game.getInstance().getAllPlayer().length; i++){
                msg += Integer.toString(n.getPF(Game.getInstance().getPlayer(i))) + "\t\t";
            }
            file.WriteFile(blank + msg);
        }
    }

    @SuppressLint("NewApi")
    private void writeActions(Actions actions, String blank){
        LogFile file = LogFile.getInstance();
        file.WriteFile(blank + "--- Placing PF ----------------------------");
        for (Map.Entry<Nations, Integer> entry : actions.getPlacePF().entrySet()){
            file.WriteFile(blank + "Location: " + entry.getKey().getName() + ", PF: " + Integer.toString(entry.getValue()));
        }
        file.WriteFile(blank + "--- Attacking ------------------------------");
        for (Map.Entry<Nations, Players> entry : actions.getAttack().entrySet()){
            file.WriteFile(blank + "Location: " + entry.getKey().getName() + ", Def: " + entry.getValue().getName());
        }
        file.WriteFile(blank + "---------------------------------------------");
        file.WriteFile(blank + "Total score: " + actions.getScore());
    }

    private void calculateActionsScore(Nations[] currentBoard, Players currentPlayer, Actions currentAction, int deepLvl){
        int score = 0;
        HashMap<Nations, Integer> scoreList = Score.getInstance().getScoreList(currentPlayer.getCardSelected().getName());
        Nations[] tempBoard = updateBoardTemp(currentBoard, currentPlayer, currentAction.getPlacePF());

        // get score for attacking
        if (currentAction.getAttack().size() > 0){
            int attPF, defPF;
            int bonus;
            boolean exclusive;
            for (Map.Entry<Nations, Players> entry : currentAction.getAttack().entrySet()) {
                for (Nations n : tempBoard){
                    if (n.getName().equals(entry.getKey().getName())){
                        // check EXCLUSIVE location
                        if ((currentPlayer.getCardSelected().getName().equals("Britain") && n.getId() == 11)    // Far East
                                || (currentPlayer.getCardSelected().getName().equals("France") && n.getId() == 12) // Africa
                                || (currentPlayer.getCardSelected().getName().equals("Russia") && n.getId() == 10) // Turkey
                                || (currentPlayer.getCardSelected().getName().equals("Austria") && n.getId() == 6)) // Serbia
                            exclusive = true;
                        else
                            exclusive = false;

                        /** calculate attack score **/
                        attPF = n.getPF(currentPlayer);
                        defPF = n.getPF(entry.getValue());
                        if ((attPF/defPF) >= 4)
                            score += 40;
                        else if ((attPF/defPF) >= 3)
                            score += 20;
                        else if ((attPF/defPF) >= 2)
                            score += 10;
                        else if ((attPF/defPF) >= 1)
                            score += 5;
                        else
                            score += (-10);

                        // more bonus score for DEFENDER's PF
                        if (defPF >= 10)        // GOT TREATY RIGHTS
                            if (exclusive)
                                bonus = 11;
                            else
                                bonus = 3;
                        else if (defPF >= 5)    // might get treaty rights NEXT move
                            if (exclusive)
                                bonus = 5;
                            else
                                bonus = 2;
                        else
                            bonus = 1;
                        score *= bonus;

                        // add more score for EXCLUSIVE area
                        if (exclusive)
                            score *= 10;
                    }
                }
            }

            // update TEMP board after attacking
            int numDice;
            if ((deepLvl) % 5 == 0)
                numDice = 4;                // the worst case for current player
            else
                numDice = 6;                // the best case for other players
            tempBoard = updateBoardTempAttack(tempBoard, currentPlayer, currentAction.getAttack(), numDice);
        }

        // get score for placing PF to get Treaty Right
        // get score for placing PF to protect own Victory Point
        for (Map.Entry<Nations, Integer> entry : scoreList.entrySet()){
            for (Nations n : tempBoard){
                if (n.getName().equals(entry.getKey().getName())){
                    score += scorePlacePFtoGetTP(entry.getValue(), n.getPF(currentPlayer));
                    score += scorePlacePFtoProtect(n, currentPlayer, n.getPF(currentPlayer));
                    score += scorePlacingTooMuch(n, currentPlayer);
                }
            }
        }

        // get score for placing to Prevent (France and Germany)

        /** Calculate EXCLUSIVE treaty point **/
        score += scoreExclusiveTP(tempBoard, currentPlayer);

        currentAction.setScore(score);
    }

    private void addAttacking(ArrayList<Actions> actionList, Actions action, Nations[] board, Players player){
        ArrayList<Nations> locations = new ArrayList<Nations>();
        int[] numPF = new int[action.getPlacePF().size()];
        int index = 0;

        // get placing actions
        for (Map.Entry<Nations, Integer> entry : action.getPlacePF().entrySet()){
            locations.add(entry.getKey());
            numPF[index] = entry.getValue();
            index ++;
        }

        for (Nations n : board){
            if (n.getPF(player) > 0){   // check attacker's PF
                for (Players p : Game.getInstance().getAllPlayer()){
                    if (!(p.equals(player))) {
                        if (n.getPF(p) > 0){    // check defender's PF
                            if (n.getPF(player) >= n.getPF(p)) {    // add attack once attacker's pf more than defender
                                // add new action including attacking
                                Actions newAction = addPlacingPF(locations, numPF, player);
                                newAction.setAttack(n, p);
                                actionList.add(newAction);      // add the new action to actions list
                            }
                        }
                    }
                }
            }
        }
    }

    private void distributePFLast(ArrayList<Actions> actions, ArrayList<Nations> locations, int[] pf, Players player, int lastIndex){
        int totalPF = player.getCardSelected().getNumPF();
        int atLeastLocations, totalPFForLast = 0;
        int last = lastIndex;
        int index = last - 1;
        while (pf[index] == 0)      // find the last that pf > 0
            index --;
        pf[index] --;       // decrease pf
        pf[index + 1] ++;   // increase pf the next index
        if (pf[last] == 5)  // change the last index when the last is 5 pf
            last --;
        actions.add(addPlacingPF(locations, pf, player));

        // find at least locations
        if (totalPF % 5 == 0)
            atLeastLocations = totalPF/5;
        else
            atLeastLocations = (totalPF/5) + 1;

        // check finish
        for (int i=1; i<=atLeastLocations; i++)
            totalPFForLast += pf[pf.length - i];
        if (totalPFForLast != totalPF)
            distributePFLast(actions, locations, pf, player, last);
    }

    private int findMax(int[] set){
        int max = 0;
        for (int i : set){
            if (i > max)
                max = i;
        }
        return max;
    }

    private void distributePFAvg(ArrayList<Actions> actions, ArrayList<Nations> locations, int[] pf, Players player){
        int maxPF = findMax(pf);
        int indexMax = pf.length - 1;
        while (pf[indexMax] < maxPF)      // find the last that is max
            indexMax --;
        pf[indexMax] --;       // decrease pf
        pf[indexMax + 1] ++;   // increase pf the next index
        actions.add(addPlacingPF(locations, pf, player));

        // check finish
        if (pf[pf.length - 1] < pf[indexMax])
            distributePFAvg(actions, locations, pf, player);
    }

    private void distributePFMax(ArrayList<Actions> actions, ArrayList<Nations> locations, int[] pf, Players player, int changeIndex, int lastIndex, int availablePF, int finishedPF){
        int targetPF = availablePF - finishedPF;
        int totalPF = player.getCardSelected().getNumPF();
        int atLeastLocations, totalPFForLast = 0;

        // find at least locations
        if (totalPF % 5 == 0)
            atLeastLocations = totalPF/5;
        else
            atLeastLocations = (totalPF/5) + 1;

        if (targetPF > 5)
            targetPF = 5;

        if (pf[changeIndex] == 0){
            for (int i=lastIndex - 1; i>=0; i--){
                if (pf[i] == targetPF) {
                    changeIndex = i;
                    pf[i]--;
                    pf[i + 1]++;
                    break;
                }
            }
            actions.add(addPlacingPF(locations, pf, player));

            // check finish
            for (int i=1; i<=atLeastLocations; i++)
                totalPFForLast += pf[pf.length - i];
            if (totalPFForLast != totalPF)
                distributePFMax(actions, locations, pf, player, changeIndex, lastIndex, availablePF, finishedPF);
        }
        else{
            try {
                for (int i = lastIndex - 1; i > changeIndex; i--) {
                    if (pf[i] == targetPF)
                        changeIndex = i;
                }
                pf[changeIndex]--;
                pf[changeIndex + 1]++;
                actions.add(addPlacingPF(locations, pf, player));
                if (pf[lastIndex - 1] == 5) {
                    lastIndex -= 1;
                    finishedPF += 5;
                }

                // check finish
                for (int i = 1; i <= atLeastLocations; i++)
                    totalPFForLast += pf[pf.length - i];
                if (totalPFForLast != totalPF)
                    distributePFMax(actions, locations, pf, player, changeIndex, lastIndex, availablePF, finishedPF);
            }
            catch(Exception ignored) {

            }
        }
    }

    private void distributePFGroup(ArrayList<Actions> actions, ArrayList<Nations> locations, int[] pf, Players player){
        int first = -1, last = -1;
        int totalPF = player.getCardSelected().getNumPF();
        int atLeastLocations, totalPFForLast = 0;

        // find at least locations
        if (totalPF % 5 == 0)
            atLeastLocations = totalPF/5;
        else
            atLeastLocations = (totalPF/5) + 1;

        // find first value
        for (int i=0; i<pf.length; i++){
            if (pf[i] > 0){
                first = i;
                break;
            }
        }

        // find last value
        for (int i=pf.length - 1; i>=0; i--){
            if (pf[i] > 0){
                if (pf[i] == 5)
                    last = i + 1;
                else
                    last = i;
                break;
            }
        }

        // create opportunity
        if (first != -1 && last != -1) {
            pf[first]--;
            pf[last]++;
            actions.add(addPlacingPF(locations, pf, player));

            // check finish
            for (int i=1; i<=atLeastLocations; i++)
                totalPFForLast += pf[pf.length - i];
            if (totalPFForLast != totalPF)
                distributePFGroup(actions, locations, pf, player);
        }
    }

    private Actions addPlacingPF(ArrayList<Nations> location, int[] pf, Players player){
        Actions newAction = new Actions(player);
        for (int i=0; i<location.size(); i++){
            if (pf[i] > 0)
                newAction.setPlace(location.get(i), pf[i]);
        }
        return newAction;
    }

    private ArrayList<Nations> findLocationsForPlacing(Nations[] currentBoard, Players player){
        ArrayList<Nations> locations = new ArrayList<Nations>();

        // score list
        HashMap<Nations, Integer> scoreList = Score.getInstance().getScoreList(player.getCardSelected().getName());
        for (Map.Entry<Nations, Integer> entry : scoreList.entrySet())
            locations.add(entry.getKey());

        // the nations which the most VP has got PF
        Players biggest = findBiggestScore(currentBoard, player);
        if (Score.getInstance().getScore(currentBoard, biggest) > 0) {
            scoreList = Score.getInstance().getScoreList(biggest.getCardSelected().getName());
            for (Map.Entry<Nations, Integer> entry : scoreList.entrySet()) {
                if (!(locations.contains(entry.getKey())))
                    locations.add(entry.getKey());
            }
        }

        return locations;
    }

    private Players findBiggestScore(Nations[] currentBoard, Players without){  // find the biggest score without current turn
        Players biggest;
        ArrayList<Players> all = new ArrayList<Players>();
        ArrayList<Integer> score = new ArrayList<Integer>();
        for (Players p : Game.getInstance().getAllPlayer()){
            if (!(p.equals(without))){
                all.add(p);
                score.add(Score.getInstance().getScore(currentBoard, p));
            }
        }
        biggest = all.get(score.indexOf(Collections.max(score)));
        return biggest;
    }

    /************ Pure Minimax ************/
    @SuppressLint("NewApi")
    public Actions pureMinimaxDecision(Board board, final Players player, int maxDeep, int maxChance, boolean writeLog){
        Actions actionSelected;
        ArrayList<Actions> allActions = createOpportunityWithoutScore(board.getBoard(), player, maxChance, 0);

        // create temp board with the new actions
        final ArrayList<Nations[]> tmpBoardList = new ArrayList<Nations[]>();
        final ArrayList<Nations[]> tmpBoardListScore = new ArrayList<Nations[]>();

        for (int i=0; i<allActions.size() ; i++){
            Actions tmpAction = allActions.get(i);
            Nations[] tmpBoard = updateBoardTemp(board.getBoard(), player, tmpAction.getPlacePF());
            if (tmpAction.getAttack().size() > 0){
                int numDice;
//                if ((deepLvl - 1) % 5 == 0)
                numDice = 1;                // the worst case for current player
//                else
//                    numDice = 6;                // the best case for other players
                tmpBoard = updateBoardTempAttack(tmpBoard, player, tmpAction.getAttack(), numDice);
            }

            tmpBoardList.add(tmpBoard);

            /** Write to file **/
            LogFile file = LogFile.getInstance();
            if (writeLog) {
                file.WriteFile("********** Player: " + player.getCardSelected().getName() + " -> Level: " + 0 + " **********");
                writeActions(tmpAction, "");
                file.WriteFile("******************************************");

                writeCurrentBoard(tmpBoard, player, 0, "");
            }

            // minimax last board
            tmpBoardListScore.add(pureMinimaxLastBoard(tmpBoardList.get(i), player, maxDeep, 1, maxChance, writeLog));
        }

        // calculate score and choose
        ArrayList<Integer> scoreList = new ArrayList<Integer>();
        for (int i=0; i<tmpBoardListScore.size(); i++){
            scoreList.add(Score.getInstance().getScore(tmpBoardListScore.get(i), player));
        }

        // add max score to the list
        ArrayList<Integer> indexSelectedList = new ArrayList<Integer>();
        int scoreSelected = Collections.max(scoreList);
        for (int i=0; i<scoreList.size(); i++){
            if (scoreList.get(i).equals(scoreSelected))
                indexSelectedList.add(i);
        }

        // random actionSelected from the list
        Random r = new Random();
        int randomIndex = r.nextInt(indexSelectedList.size());
        actionSelected = allActions.get(indexSelectedList.get(randomIndex));

        /** Write to file **/
        if (writeLog) {
            LogFile file = LogFile.getInstance();
            for (int i = 0; i < tmpBoardListScore.size(); i++) {
                file.WriteFile("## total VP of board " + i + ": " + scoreList.get(i));
            }

            file.WriteFile("### index selected: " + indexSelectedList.get(randomIndex) + " with VP: " + scoreList.get(indexSelectedList.get(randomIndex)));
        }

        return actionSelected;
    }

    @SuppressLint("NewApi")
    public Nations[] pureMinimaxLastBoard(Nations[] currentBoard, Players player, int maxDeep, int deepLvl, int maxChance, boolean writeLog){
        Players currentTurn = Board.getInstance().getCurrentTurn();
        if ((deepLvl < maxDeep) && (currentTurn.getTotalTurn() + (deepLvl/(5 - currentTurn.getCardSelected().getId()))) <= 10){ //&& player.getTotalTurn() <= 10) {
            Players nextTurn = null;
            int cardID = (player.getCardSelected().getId() + 1) % Board.getInstance().getNumPlayers(); // next turn (order by card selected)

            // find the next turn
            for (Players p : Game.getInstance().getAllPlayer()) {
                if (p.getCardSelected().getId() == cardID) {
                    nextTurn = p;
                    break;
                }
            }

            // generate next board
            ArrayList<Actions> allActions = createOpportunityWithoutScore(currentBoard, nextTurn, maxChance, deepLvl);

            // create temp board with the new actions
            ArrayList<Nations[]> tmpBoardList = new ArrayList<Nations[]>();
            ArrayList<Nations[]> tmpBoardListScore = new ArrayList<Nations[]>();

            for (int i=0; i<allActions.size(); i++) {
                Actions tmpAction = allActions.get(i);
                Nations[] tmpBoard = updateBoardTemp(currentBoard, nextTurn, tmpAction.getPlacePF());
                if (tmpAction.getAttack().size() > 0) {
                    int numDice;
                    if ((deepLvl) % 5 == 0)
                        numDice = 4;                // the worst case for current player
                    else
                        numDice = 6;                // the best case for other players
                    tmpBoard = updateBoardTempAttack(tmpBoard, nextTurn, tmpAction.getAttack(), numDice);
                }

                tmpBoardList.add(tmpBoard);

                /** Write to file **/
                LogFile file = LogFile.getInstance();
                if (writeLog) {
                    String blank = "";
                    for (int j = 0; j < deepLvl; j++)
                        blank += "\t";
                    file.WriteFile(blank + "********** Player: " + nextTurn.getCardSelected().getName() + " -> Level: " + (deepLvl) + " **********");
                    writeActions(tmpAction, blank);
                    file.WriteFile(blank + "******************************************");

                    writeCurrentBoard(tmpBoard, nextTurn, (deepLvl), blank);
                }

                // minimax last board -> going MORE deep or adding TREATY RIGHT
                tmpBoardListScore.add(pureMinimaxLastBoard(tmpBoardList.get(i), nextTurn, maxDeep, deepLvl + 1, maxChance, writeLog));
            }

            // calculate score and choose
            ArrayList<Integer> scoreList = new ArrayList<Integer>();
            Nations[] boardSelected;
            for (int i=0; i<tmpBoardListScore.size(); i++){
                scoreList.add(Score.getInstance().getScore(tmpBoardListScore.get(i), nextTurn));
            }

            ArrayList<Integer> indexSelectedList = new ArrayList<Integer>();
            int scoreSelected;
            if ((deepLvl) % 5 == 0) {       /** Select Max for current AI **/
                scoreSelected = Collections.max(scoreList);
                for (int i=0; i<scoreList.size(); i++){
                    if (scoreList.get(i).equals(scoreSelected))
                        indexSelectedList.add(i);
                }
            }
            else {                              /** Select Min for other turn **/
                scoreSelected = Collections.min(scoreList);
                for (int i=0; i<scoreList.size(); i++){
                    if (scoreList.get(i).equals(scoreSelected))
                        indexSelectedList.add(i);
                }
            }

            // random boardSelected from the list
            Random r = new Random();
            int indexSelected = indexSelectedList.get(r.nextInt(indexSelectedList.size()));
            boardSelected = tmpBoardListScore.get(indexSelected);

            /** Write to file **/
            if (writeLog) {
                String blank = "";
                for (int j = 0; j < deepLvl; j++)
                    blank += "\t";
                LogFile file = LogFile.getInstance();
                for (int i = 0; i < tmpBoardListScore.size(); i++) {
                    file.WriteFile(blank + "## total VP of board " + i + ": " + scoreList.get(i));
                }

                file.WriteFile(blank + "### index selected: " + indexSelected + " with VP: " + scoreList.get(indexSelected));
            }

            return boardSelected;

        }else {
            // set treaty right
            for (Nations n : currentBoard) {
                for (Players p : Game.getInstance().getAllPlayer()) {
                    if (n.getPF(p) >= 10)
                        n.addTreatyRight(p);
                }
            }

            return currentBoard;
        }
    }

    @SuppressLint("NewApi")
    private ArrayList<Actions> createOpportunityWithoutScore(Nations[] board, Players currentTurn, int maxChance, int deepLvl){
        Players player = currentTurn;
        ArrayList<Actions> actions = new ArrayList<Actions>();

        /***** CreateOpportunity *****/
        /** Place PF **/

        // find the nations which should place
        ArrayList<Nations> locations = findLocationsForPlacing(board, player);
        int[] placePF = new int[locations.size()];

        // get the number of PF
        int availablePF = player.getCardSelected().getNumPF();
        int maxPF = 5;  // the maximum pf for placing per location per turn

        // place PF by order of those nations
        availablePF = player.getCardSelected().getNumPF();
        for (int i=0; i<locations.size(); i++){
            if (availablePF > maxPF) {
                placePF[i] = maxPF;
                availablePF -= maxPF;
            }
            else{
                placePF[i] = availablePF;
                availablePF -= availablePF;
            }
        }
        actions.add(addPlacingPF(locations, placePF, player));

        // for the other
        int last = locations.size() - 1;
//        distributePFLast(actions, locations, placePF.clone(), player, last);
//        distributePFAvg(actions, locations, placePF.clone(), player);
//        distributePFMax(actions, locations, placePF.clone(), player, placePF.length - 1, placePF.length, player.getCardSelected().getNumPF(), 0);
        distributePFGroup(actions, locations, placePF.clone(), player);

        /** Attacking **/
        int placePFLength = actions.size();
        for (int i=0; i<placePFLength; i++){
            Nations[] tempBoard = updateBoardTemp(board, player, actions.get(i).getPlacePF());
            addAttacking(actions, actions.get(i), tempBoard, player);
        }

        return actions;
    }

    /************ Random Minimax ************/
    @SuppressLint("NewApi")
    public Actions randomMinimaxDecision(Board board, final Players player, int maxDeep, int maxChance, boolean writeLog){
        Actions actionSelected;
        ArrayList<Actions> allActions = createOpportunityRandom(board.getBoard(), player, maxChance, 0);

        // create temp board with the new actions
        final ArrayList<Nations[]> tmpBoardList = new ArrayList<Nations[]>();
        final ArrayList<Nations[]> tmpBoardListScore = new ArrayList<Nations[]>();

        // ab-pruning condition
        int[] alphaScore = new int[5];
        alphaScore[0] = -1; // initial score

        for (int i=0; i<allActions.size() ; i++){
            Actions tmpAction = allActions.get(i);
            Nations[] tmpBoard = updateBoardTemp(board.getBoard(), player, tmpAction.getPlacePF());
            if (tmpAction.getAttack().size() > 0){
                int numDice;
//                if ((deepLvl - 1) % 5 == 0)
                numDice = 1;                // the worst case for current player
//                else
//                    numDice = 6;                // the best case for other players
                tmpBoard = updateBoardTempAttack(tmpBoard, player, tmpAction.getAttack(), numDice);
            }

            tmpBoardList.add(tmpBoard);

            /** Write to file **/
            LogFile file = LogFile.getInstance();
            if (writeLog) {
                file.WriteFile("********** Player: " + player.getCardSelected().getName() + " -> Level: " + 0 + " **********");
                writeActions(tmpAction, "");
                file.WriteFile("******************************************");

                writeCurrentBoard(tmpBoard, player, 0, "");
            }

            // minimax last board
            tmpBoardListScore.add(randomMinimaxLastBoard(tmpBoardList.get(i), player, maxDeep, 1, maxChance, alphaScore, writeLog));
        }

        // calculate score and choose
        ArrayList<Integer> scoreList = new ArrayList<Integer>();
        for (int i=0; i<tmpBoardListScore.size(); i++){
            scoreList.add(Score.getInstance().getScore(tmpBoardListScore.get(i), player));
        }

        // add max score to the list
        ArrayList<Integer> indexSelectedList = new ArrayList<Integer>();
        int scoreSelected = Collections.max(scoreList);
        for (int i=0; i<scoreList.size(); i++){
            if (scoreList.get(i).equals(scoreSelected))
                indexSelectedList.add(i);
        }

        // random actionSelected from the list
        Random r = new Random();
        int randomIndex = r.nextInt(indexSelectedList.size());
        actionSelected = allActions.get(indexSelectedList.get(randomIndex));

        /** Write to file **/
        if (writeLog) {
            LogFile file = LogFile.getInstance();
            for (int i = 0; i < tmpBoardListScore.size(); i++) {
                file.WriteFile("## total VP of board " + i + ": " + scoreList.get(i));
            }

            file.WriteFile("### index selected: " + indexSelectedList.get(randomIndex) + " with VP: " + scoreList.get(indexSelectedList.get(randomIndex)));
        }

        return actionSelected;
    }

    @SuppressLint("NewApi")
    public Nations[] randomMinimaxLastBoard(Nations[] currentBoard, Players player, int maxDeep, int deepLvl, int maxChance, int[] alphaScore, boolean writeLog){
        Players currentTurn = Board.getInstance().getCurrentTurn();
        if ((deepLvl < maxDeep) && (currentTurn.getTotalTurn() + (deepLvl/(5 - currentTurn.getCardSelected().getId()))) <= 10){ //&& player.getTotalTurn() <= 10) {
            Players nextTurn = null;
            int cardID = (player.getCardSelected().getId() + 1) % Board.getInstance().getNumPlayers(); // next turn (order by card selected)

            // find the next turn
            for (Players p : Game.getInstance().getAllPlayer()) {
                if (p.getCardSelected().getId() == cardID) {
                    nextTurn = p;
                    break;
                }
            }

            // generate next board
            ArrayList<Actions> allActions = createOpportunityRandom(currentBoard, nextTurn, maxChance, deepLvl);

            // create temp board with the new actions
            ArrayList<Nations[]> tmpBoardList = new ArrayList<Nations[]>();
            ArrayList<Nations[]> tmpBoardListScore = new ArrayList<Nations[]>();

            // ab-pruning condition
            int[] betaScore = new int[5];
            int indexPlayer;
            Nations[] pruningBoard, boardSelected = null;
            boolean selected = true;

            betaScore[0] = -1; // initial score

            for (int i=0; i<allActions.size(); i++) {
                Actions tmpAction = allActions.get(i);
                Nations[] tmpBoard = updateBoardTemp(currentBoard, nextTurn, tmpAction.getPlacePF());
                if (tmpAction.getAttack().size() > 0) {
                    int numDice;
                    if ((deepLvl) % 5 == 0)
                        numDice = 4;                // the worst case for current player
                    else
                        numDice = 6;                // the best case for other players
                    tmpBoard = updateBoardTempAttack(tmpBoard, nextTurn, tmpAction.getAttack(), numDice);
                }

                tmpBoardList.add(tmpBoard);

                /** Write to file **/
                LogFile file = LogFile.getInstance();
                if (writeLog) {
                    String blank = "";
                    for (int j = 0; j < deepLvl; j++)
                        blank += "\t";
                    file.WriteFile(blank + "********** Player: " + nextTurn.getCardSelected().getName() + " -> Level: " + (deepLvl) + " **********");
                    writeActions(tmpAction, blank);
                    file.WriteFile(blank + "******************************************");

                    writeCurrentBoard(tmpBoard, nextTurn, (deepLvl), blank);
                }

                // minimax last board -> going MORE deep or adding TREATY RIGHT
                indexPlayer = nextTurn.getId();
                pruningBoard = randomMinimaxLastBoard(tmpBoardList.get(i), nextTurn, maxDeep, deepLvl + 1, maxChance, betaScore, writeLog);
                if (i == 0) {   // the first node in each level
                    for (int j = 0; j < betaScore.length; j++) {
                        betaScore[j] = Score.getInstance().getScore(pruningBoard, Game.getInstance().getPlayer(j));
                    }
                    boardSelected = pruningBoard;

                    /** Checking pruning **/
                    if (alphaScore[0] != -1) {
                        int indexBefore = indexPlayer - 1;
                        if (indexBefore == -1)
                            indexBefore = 4;
                        if ((betaScore[indexPlayer] > alphaScore[indexPlayer]) || (betaScore[indexBefore] < alphaScore[indexBefore])) {
                            // cut child as pruning
                            break;
                        }
                    }

                }
                else{       // other nodes
                    for (int j=0; j < betaScore.length; j++){
                        int score = Score.getInstance().getScore(pruningBoard, Game.getInstance().getPlayer(j));
                        if (j == indexPlayer){  // the score have to equal or more
                            if (score < betaScore[j]) {
                                selected = false;
                                break;
                            }
                        }
                    }
                    if (selected) {
                        for (int j = 0; j < betaScore.length; j++) {
                            betaScore[j] = Score.getInstance().getScore(pruningBoard, Game.getInstance().getPlayer(j));
                        }
                        boardSelected = pruningBoard;

                        /** Checking pruning **/
                        if (alphaScore[0] != -1) {
                            int indexBefore = indexPlayer - 1;
                            if (indexBefore == -1)
                                indexBefore = 4;
                            if ((betaScore[indexPlayer] > alphaScore[indexPlayer]) || (betaScore[indexBefore] < alphaScore[indexBefore])) {
                                // cut child as pruning
                                break;
                            }
                        }
                    }
                }
            }

            return boardSelected;

        }else {
            // set treaty right
            for (Nations n : currentBoard) {
                for (Players p : Game.getInstance().getAllPlayer()) {
                    if (n.getPF(p) >= 10)
                        n.addTreatyRight(p);
                }
            }

            return currentBoard;
        }
    }

    @SuppressLint("NewApi")
    private ArrayList<Actions> createOpportunityRandom(Nations[] board, Players currentTurn, int maxChance, int deepLvl){
        Players player = currentTurn;
        ArrayList<Actions> actions = new ArrayList<Actions>();

        /***** CreateOpportunity *****/
        /** Place PF **/

        // find the nations which should place
        ArrayList<Nations> locations = findLocationsForPlacing(board, player);
        int[] placePF = new int[locations.size()];

        // get the number of PF
        int availablePF = player.getCardSelected().getNumPF();
        int maxPF = 5;  // the maximum pf for placing per location per turn

        // place PF by order of those nations
        availablePF = player.getCardSelected().getNumPF();
        // for the first location -> first opportunity
        for (int i=0; i<locations.size(); i++){
            if (availablePF > maxPF) {
                placePF[i] = maxPF;
                availablePF -= maxPF;
            }
            else{
                placePF[i] = availablePF;
                availablePF -= availablePF;
            }
        }
        actions.add(addPlacingPF(locations, placePF, player));

        // for the other
        int last = locations.size() - 1;
        distributePFLast(actions, locations, placePF.clone(), player, last);
        distributePFAvg(actions, locations, placePF.clone(), player);
        distributePFMax(actions, locations, placePF.clone(), player, placePF.length - 1, placePF.length, player.getCardSelected().getNumPF(), 0);
        distributePFGroup(actions, locations, placePF.clone(), player);

        /** Attacking **/
        int placePFLength = actions.size();
        for (int i=0; i<placePFLength; i++){
            Nations[] tempBoard = updateBoardTemp(board, player, actions.get(i).getPlacePF());
            addAttacking(actions, actions.get(i), tempBoard, player);
        }

        /** Random cases **/
        ArrayList<Actions> actionsSelected = new ArrayList<Actions>();
        Random r = new Random();
        int indexRandom;
        for (int i=0; i<maxChance; i++){
            indexRandom = r.nextInt(actions.size());
            actionsSelected.add(actions.get(indexRandom));
            actions.remove(indexRandom);
        }

        return actionsSelected;
    }
}
