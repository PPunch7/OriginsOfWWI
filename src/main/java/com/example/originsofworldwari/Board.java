package com.example.originsofworldwari;

public class Board {
    private static Board instance;
    private int numPlayers;
    private int numNations;
    private Players currentTurn;
    private String currentAction;
    private int currentAvailablePF;
    private Nations[] board;
    private Players currentDef;
    private int currentAttPF, currentDefPF, currentNumDice;
    private String currentAttType;
    private Actions currentAIActions;

    private Board(){
        this.numPlayers = 5;
        this.numNations = 13;
        addBoard();
    }

    public static Board getInstance(){
        if (instance == null)
            instance = new Board();
        return instance;
    }

    private void addBoard(){
        board = new Nations[this.numNations];
        board[0] = new Nations(0, "Britain", true);
        board[1] = new Nations(1, "France", true);
        board[2] = new Nations(2, "Germany", true);
        board[3] = new Nations(3, "Russia", true);
        board[4] = new Nations(4, "Austria", true);
        board[5] = new Nations(5, "Italy", true);
        board[6] = new Nations(6, "Serbia", true);
        board[7] = new Nations(7, "Romania", true);
        board[8] = new Nations(8, "Bulgaria", true);
        board[9] = new Nations(9, "Greece", true);
        board[10] = new Nations(10, "Turkey", true);
        board[11] = new Nations(11, "Far East", true);
        board[12] = new Nations(12, "Africa", true);
    }

    public void clearBoard(){
        for (Nations n : this.board)
            n.clearPF();
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public Players getCurrentTurn(){
        return this.currentTurn;
    }

    public void setCurrentTurn(Players turn){ this.currentTurn = turn; }

    public String getCurrentAction() { return this.currentAction; }

    public void setCurrentAction(String action){ this.currentAction = action; }

    public int getCurrentAvailablePF() { return this.currentAvailablePF; }

    public void setCurrentAvailablePF(int pf) { this.currentAvailablePF = pf; }

    public Players getCurrentDef() { return this.currentDef; }

    public void setCurrentDef(Players def) { this.currentDef = def; }

    public Nations[] getBoard(){
        return this.board;
    }

    public void setBoard(Nations[] currentBoard){
        this.board = currentBoard;
    }

    public Nations getNations(int index) { return this.board[index]; }

    public int getCurrentAttPF() { return this.currentAttPF; }

    public void setCurrentAttPF(int pf) { this.currentAttPF = pf; }

    public int getCurrentDefPF() { return this.currentDefPF; }

    public void setCurrentDefPF(int pf) { this.currentDefPF = pf; }

    public int getCurrentNumDice() { return this.currentNumDice; }

    public void setCurrentNumDice(int num) { this.currentNumDice = num; }

    public String getCurrentAttType() { return this.currentAttType; }

    public void setCurrentAttType(String type) { this.currentAttType = type; }

    public Actions getCurrentAIActions() { return this.currentAIActions; }

    public void setCurrentAIActions(Actions actions) { this.currentAIActions = actions; }
}
