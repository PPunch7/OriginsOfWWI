package com.example.originsofworldwari;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "nationsClicked";
    private Game game;
    private Board board;
    private Button[] btnNation = new Button[13];
    private TextView[][] txtDisplay = new TextView[13][5];      // for txtView to display
    private int nationSelected = 0;                             // for checking and display from btn clicked
    private ArrayList<HashMap<String, Integer>> history;        // store to undo
    private boolean map;                                        // mode to display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = Game.getInstance();
        board = Board.getInstance();
        history = game.getHistoryTurns();

        // first start
        this.map = true;
        displayMap();

        game.startGame();
        setNationColour();
        addBtnNations();
        addTxtView();
        displayPlayer();
        displayAction();

        enableAllButton();
        displayNumPFByNations();
        updateNumPF();

        clearHistory();

        if (this.board.getCurrentTurn().getIsAI()) {

            // inactive all buttons
            inactiveAllButtons();

            aiMove();
        }
    }

    private void displayMap(){
        LinearLayout layMap = (LinearLayout) findViewById(R.id.layMap);
        TableLayout tblOverview = (TableLayout) findViewById(R.id.tblOverview);
        ImageView imgSwitch = (ImageView) findViewById(R.id.imgSwitch);
        if (this.map){
            layMap.setVisibility(View.VISIBLE);
            tblOverview.setVisibility(View.GONE);
            imgSwitch.setImageDrawable(getResources().getDrawable(R.drawable.table_icon));
        }
        else{
            layMap.setVisibility(View.GONE);
            tblOverview.setVisibility(View.VISIBLE);
            imgSwitch.setImageDrawable(getResources().getDrawable(R.drawable.map_icon));
        }
    }

    private void setNationColour(){
        TextView lblBritain, lblFrance, lblGermany, lblRussia, lblAustria;
        lblBritain = (TextView) findViewById(R.id.lblBritain);
        lblFrance = (TextView) findViewById(R.id.lblFrance);
        lblGermany = (TextView) findViewById(R.id.lblGermany);
        lblRussia = (TextView) findViewById(R.id.lblRussia);
        lblAustria = (TextView) findViewById(R.id.lblAustria);

        setTxtColour(lblBritain, this.game.getPlayer(0).getColour());
        setTxtColour(lblFrance, this.game.getPlayer(1).getColour());
        setTxtColour(lblGermany, this.game.getPlayer(2).getColour());
        setTxtColour(lblRussia, this.game.getPlayer(3).getColour());
        setTxtColour(lblAustria, this.game.getPlayer(4).getColour());
    }

    private void setTxtColour(TextView txt, String colour){
        switch (colour) {
            case "green":
                txt.setTextColor(Color.parseColor("#FF2C9F00"));
                break;
            case "blue":
                txt.setTextColor(Color.parseColor("#FF26A8FF"));
                break;
            case "orange":
                txt.setTextColor(Color.parseColor("#FFEA5600"));
                break;
            case "pink":
                txt.setTextColor(Color.parseColor("#FFE50BF1"));
                break;
            case "black":
                txt.setTextColor(Color.parseColor("#FF000000"));
                break;
        }
    }

    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    private void displayPlayer(){
        Players currentPlayer = board.getCurrentTurn();
        int currentAvailablePF = board.getCurrentAvailablePF();
        TextView txtPlayer, txtNation, txtTurn, txtPF, txtMaxPF;
        ImageView imgFlag;

        txtPlayer = (TextView) findViewById(R.id.txtCurrentTurn);
        txtNation = (TextView) findViewById(R.id.txtNation);
        imgFlag = (ImageView) findViewById(R.id.imgFlag);
        txtTurn = (TextView) findViewById(R.id.txtTotalTurn);
        txtPF = (TextView) findViewById(R.id.txtAvailablePF);
        txtMaxPF = (TextView) findViewById(R.id.txtMaxPF);

        txtPlayer.setText(currentPlayer.getName());
        setTxtColour(txtPlayer, currentPlayer.getColour());
        txtNation.setText(currentPlayer.getCardSelected().getName());
        txtTurn.setText(Integer.toString(currentPlayer.getTotalTurn()));
        txtPF.setText(Integer.toString(currentAvailablePF));
        txtMaxPF.setText(Integer.toString(currentPlayer.getCardSelected().getNumPF()));

        // set flag
        if (txtNation.getText().equals("Britain"))
            imgFlag.setBackground(getResources().getDrawable(R.drawable.flag_of_the_united_kingdom));
        else if (txtNation.getText().equals("France"))
            imgFlag.setBackground(getResources().getDrawable(R.drawable.flag_of_france));
        else if (txtNation.getText().equals("Germany"))
            imgFlag.setBackground(getResources().getDrawable(R.drawable.flag_of_germany));
        else if (txtNation.getText().equals("Russia"))
            imgFlag.setBackground(getResources().getDrawable(R.drawable.flag_of_russia));
        else if (txtNation.getText().equals("Austria"))
            imgFlag.setBackground(getResources().getDrawable(R.drawable.flag_of_austria_hungary));
    }

    private void displayAction(){
        String currentAction = this.board.getCurrentAction();
        TextView txtPlacePF, txtAttack;

        txtPlacePF = (TextView) findViewById(R.id.txtPlacePF);
        txtAttack = (TextView) findViewById(R.id.txtAttack);

        if (currentAction.equals("PlacePF")){
            txtPlacePF.setBackgroundColor(Color.parseColor("#FF36A100"));
            txtAttack.setTextColor(Color.parseColor("#8A000000"));
            txtAttack.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        else if (currentAction.equals("Attack")){
            txtAttack.setTextColor(Color.parseColor("#FFFFFFFF"));
            txtAttack.setBackgroundColor(Color.parseColor("#FFFF0000"));
            txtPlacePF.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            disableAllButton();
            // enable the nations that has the current turn's PF only
            for (Nations n : this.board.getBoard()){
                if (n.getPF(this.board.getCurrentTurn()) > 0)
                    enableButton(btnNation[n.getId()]);
            }
        }
    }

    private void disableButton(Button btn){
        btn.setClickable(false);
        btn.setEnabled(false);
    }

    private void disableAllButton(){
        for (Button b : this.btnNation)
            disableButton(b);
    }

    private void enableButton(Button btn){
        btn.setClickable(true);
        btn.setEnabled(true);
    }

    private void enableAllButton(){
        for (Button b : this.btnNation)
            enableButton(b);
    }

    private void inactiveAllButtons(){
        Button btnEnd = (Button) findViewById(R.id.btnEndTurn);
        disableButton(btnEnd);
        for (Button b : btnNation)
            disableButton(b);
    }

    private void activeAllButtons(){
        Button btnEnd = (Button) findViewById(R.id.btnEndTurn);
        enableButton(btnEnd);
        for (Button b : btnNation)
            enableButton(b);
    }

    private void addBtnNations(){
        btnNation[0] = (Button) findViewById(R.id.btnBritain);
        btnNation[1] = (Button) findViewById(R.id.btnFrance);
        btnNation[2] = (Button) findViewById(R.id.btnGermany);
        btnNation[3] = (Button) findViewById(R.id.ibtnRussiaCard);
        btnNation[4] = (Button) findViewById(R.id.ibtnAustriaCard);
        btnNation[5] = (Button) findViewById(R.id.btnItaly);
        btnNation[6] = (Button) findViewById(R.id.btnSerbia);
        btnNation[7] = (Button) findViewById(R.id.btnRomania);
        btnNation[8] = (Button) findViewById(R.id.btnBulgaria);
        btnNation[9] = (Button) findViewById(R.id.btnGreece);
        btnNation[10] = (Button) findViewById(R.id.btnTurkey);
        btnNation[11] = (Button) findViewById(R.id.btnFarEast);
        btnNation[12] = (Button) findViewById(R.id.btnAfrica);
    }

    private void addTxtView(){
        // for Britain
        txtDisplay[0][0] = (TextView) findViewById(R.id.txtBB);
        txtDisplay[0][1] = (TextView) findViewById(R.id.txtBF);
        txtDisplay[0][2] = (TextView) findViewById(R.id.txtBG);
        txtDisplay[0][3] = (TextView) findViewById(R.id.txtBR);
        txtDisplay[0][4] = (TextView) findViewById(R.id.txtBA);

        // for France
        txtDisplay[1][0] = (TextView) findViewById(R.id.txtFB);
        txtDisplay[1][1] = (TextView) findViewById(R.id.txtFF);
        txtDisplay[1][2] = (TextView) findViewById(R.id.txtFG);
        txtDisplay[1][3] = (TextView) findViewById(R.id.txtFR);
        txtDisplay[1][4] = (TextView) findViewById(R.id.txtFA);

        // for Germany
        txtDisplay[2][0] = (TextView) findViewById(R.id.txtGB);
        txtDisplay[2][1] = (TextView) findViewById(R.id.txtGF);
        txtDisplay[2][2] = (TextView) findViewById(R.id.txtGG);
        txtDisplay[2][3] = (TextView) findViewById(R.id.txtGR);
        txtDisplay[2][4] = (TextView) findViewById(R.id.txtGA);

        // for Russia
        txtDisplay[3][0] = (TextView) findViewById(R.id.txtRB);
        txtDisplay[3][1] = (TextView) findViewById(R.id.txtRF);
        txtDisplay[3][2] = (TextView) findViewById(R.id.txtRG);
        txtDisplay[3][3] = (TextView) findViewById(R.id.txtRR);
        txtDisplay[3][4] = (TextView) findViewById(R.id.txtRA);

        // for Austria
        txtDisplay[4][0] = (TextView) findViewById(R.id.txtAB);
        txtDisplay[4][1] = (TextView) findViewById(R.id.txtAF);
        txtDisplay[4][2] = (TextView) findViewById(R.id.txtAG);
        txtDisplay[4][3] = (TextView) findViewById(R.id.txtAR);
        txtDisplay[4][4] = (TextView) findViewById(R.id.txtAA);

        // for Italy
        txtDisplay[5][0] = (TextView) findViewById(R.id.txtIB);
        txtDisplay[5][1] = (TextView) findViewById(R.id.txtIF);
        txtDisplay[5][2] = (TextView) findViewById(R.id.txtIG);
        txtDisplay[5][3] = (TextView) findViewById(R.id.txtIR);
        txtDisplay[5][4] = (TextView) findViewById(R.id.txtIA);

        // for Serbia
        txtDisplay[6][0] = (TextView) findViewById(R.id.txtSB);
        txtDisplay[6][1] = (TextView) findViewById(R.id.txtSF);
        txtDisplay[6][2] = (TextView) findViewById(R.id.txtSG);
        txtDisplay[6][3] = (TextView) findViewById(R.id.txtSR);
        txtDisplay[6][4] = (TextView) findViewById(R.id.txtSA);

        // for Romania
        txtDisplay[7][0] = (TextView) findViewById(R.id.txtRoB);
        txtDisplay[7][1] = (TextView) findViewById(R.id.txtRoF);
        txtDisplay[7][2] = (TextView) findViewById(R.id.txtRoG);
        txtDisplay[7][3] = (TextView) findViewById(R.id.txtRoR);
        txtDisplay[7][4] = (TextView) findViewById(R.id.txtRoA);

        // for Bulgaria
        txtDisplay[8][0] = (TextView) findViewById(R.id.txtBuB);
        txtDisplay[8][1] = (TextView) findViewById(R.id.txtBuF);
        txtDisplay[8][2] = (TextView) findViewById(R.id.txtBuG);
        txtDisplay[8][3] = (TextView) findViewById(R.id.txtBuR);
        txtDisplay[8][4] = (TextView) findViewById(R.id.txtBuA);

        // for Greece
        txtDisplay[9][0] = (TextView) findViewById(R.id.txtGrB);
        txtDisplay[9][1] = (TextView) findViewById(R.id.txtGrF);
        txtDisplay[9][2] = (TextView) findViewById(R.id.txtGrG);
        txtDisplay[9][3] = (TextView) findViewById(R.id.txtGrR);
        txtDisplay[9][4] = (TextView) findViewById(R.id.txtGrA);

        // for Turkey
        txtDisplay[10][0] = (TextView) findViewById(R.id.txtTB);
        txtDisplay[10][1] = (TextView) findViewById(R.id.txtTF);
        txtDisplay[10][2] = (TextView) findViewById(R.id.txtTG);
        txtDisplay[10][3] = (TextView) findViewById(R.id.txtTR);
        txtDisplay[10][4] = (TextView) findViewById(R.id.txtTA);

        // for Far East
        txtDisplay[11][0] = (TextView) findViewById(R.id.txtFEB);
        txtDisplay[11][1] = (TextView) findViewById(R.id.txtFEF);
        txtDisplay[11][2] = (TextView) findViewById(R.id.txtFEG);
        txtDisplay[11][3] = (TextView) findViewById(R.id.txtFER);
        txtDisplay[11][4] = (TextView) findViewById(R.id.txtFEA);

        // for Africa
        txtDisplay[12][0] = (TextView) findViewById(R.id.txtAfB);
        txtDisplay[12][1] = (TextView) findViewById(R.id.txtAfF);
        txtDisplay[12][2] = (TextView) findViewById(R.id.txtAfG);
        txtDisplay[12][3] = (TextView) findViewById(R.id.txtAfR);
        txtDisplay[12][4] = (TextView) findViewById(R.id.txtAfA);
    }

    @SuppressLint("SetTextI18n")
    private void displayNumPFByNations(){
        Nations nation;

        // update PF
        nation = this.board.getNations(this.nationSelected);
        for (int i=0; i<5; i++){
            txtDisplay[this.nationSelected][i].setText(Integer.toString(nation.getPF(this.game.getPlayer(i))));
            if (nation.getPF(this.game.getPlayer(i)) >= 10)
                txtDisplay[this.nationSelected][i].setTextColor(Color.parseColor("#FF0030A3"));
            else if (nation.getPF(this.game.getPlayer(i)) >= 1)
                txtDisplay[this.nationSelected][i].setTextColor(Color.parseColor("#FF000000"));
            else
                txtDisplay[this.nationSelected][i].setTextColor(Color.parseColor("#8A000000"));
        }
    }

    private void updateNumPF(){
        Nations nation;
        for (int i=0; i<this.board.getBoard().length; i++){
            nation = this.board.getNations(i);
            for (int j=0; j<5; j++){
                txtDisplay[i][j].setText((Integer.toString(nation.getPF(this.game.getPlayer(j)))));
                if (nation.getPF(this.game.getPlayer(j)) >= 10)
                    txtDisplay[i][j].setTextColor(Color.parseColor("#FF0030A3"));
                else if (nation.getPF(this.game.getPlayer(j)) >= 1)
                    txtDisplay[i][j].setTextColor(Color.parseColor("#FF000000"));
                else
                    txtDisplay[i][j].setTextColor(Color.parseColor("#8A000000"));
            }
        }
    }

    public void onNationsBtn_Click(View view){
        Button btn = (Button) findViewById(view.getId());
        String nation = btn.getText().toString();
        this.nationSelected = Integer.parseInt(btn.getContentDescription().toString());

        popupNations(nation);
    }

    public void onMapBtn_Click(View view){
        ImageView img = (ImageView) findViewById(view.getId());
        this.nationSelected = Integer.parseInt(img.getContentDescription().toString());
        String nation = this.board.getBoard()[this.nationSelected].getName();

        popupNations(nation);
    }

    private void popupNations(String nation){
        // show popup
        Intent intentActions = new Intent(GameActivity.this, ActionActivity.class);
        intentActions.putExtra(EXTRA_MESSAGE, nation);
        GameActivity.this.startActivityForResult(intentActions, 1);     // 1 is click any nation
    }

    private void setGameFinish() {
        if (game.getPlayer(board.getNumPlayers() - 1).getTotalTurn() >= 10) {    // check total turn of the last player
            game.setGameFinish();
        }
    }

    private void checkGameFinish(){
        if (game.getGameFinish()) {
            game.calTotalScore();

            Intent intentGameFinish = new Intent(GameActivity.this, EndGameActivity.class);
            intentGameFinish.putExtra(EXTRA_MESSAGE, "Game finished");
            GameActivity.this.startActivityForResult(intentGameFinish, 0);  // 0 is restart button clicked
        }
        else{
            game.nextTurn();
        }
    }

    private void clearHistory(){
        this.game.clearHistoryTurns();
        this.history = this.game.getHistoryTurns();
        displayUndoBtn();
    }

    private void addHistory(String location, int pf) {
        HashMap<String, Integer> currentAction = new HashMap<>();
        currentAction.put(location, pf);
        this.history.add(currentAction);
        this.game.setHistoryTurns(this.history);

        displayUndoBtn();
    }

    private void undoHistory(Nations[] currentBoard){
        Players currentPlayer = this.board.getCurrentTurn();
        HashMap<String, Integer> lastAction = this.history.get(this.history.size() - 1);
        for (Map.Entry<String, Integer> entry : lastAction.entrySet()){
            for (Nations n : currentBoard){
                if (n.getName().equals(entry.getKey())){
                    n.setPF(currentPlayer, (-1 * entry.getValue()));        // decrease pf in the board
                    this.board.setCurrentAvailablePF(this.board.getCurrentAvailablePF() + entry.getValue());    // give pf to available pf
                    break;
                }
            }
        }

        this.history.remove(lastAction);
        this.game.setHistoryTurns(this.history);

        displayUndoBtn();
    }

    private void displayUndoBtn(){
        Button btnUndo = (Button) findViewById(R.id.btnUndo);
        if (this.history.size() > 0){
            enableButton(btnUndo);
        }
        else {
            disableButton(btnUndo);
        }

        // display nations buttons
        enableAllButton();
        if (this.board.getCurrentAction().equals("PlacePF")) {
            for (HashMap<String, Integer> his : history) {
                for (Map.Entry<String, Integer> entry : his.entrySet()) {
                    for (Nations n : this.board.getBoard()) {
                        if (n.getName().equals(entry.getKey())) {
                            disableButton(btnNation[n.getId()]);
                        }
                    }
                }
            }
        }
    }

    public void onUndoBtn_Click(View view){
        this.board.setCurrentAction("PlacePF");
        undoHistory(this.board.getBoard());
        updateNumPF();
        displayPlayer();
        displayAction();
    }

    public void onEndTurnBtn_Click(View view){
        if (this.board.getCurrentAvailablePF() > 0) {
            // dialog message
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("You still have available PF's, would you like to end this turn?");
            dlgAlert.setTitle("Application Alert");
            dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // End Turn
                    endTurn();
                }
            });
            dlgAlert.setNegativeButton("No", null);
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
        else
            endTurn();
    }

    private void endTurn(){
        setGameFinish();
        checkGameFinish();

        displayPlayer();
        displayAction();
        enableAllButton();
        displayNumPFByNations();
        updateNumPF();

        clearHistory();

        if (this.board.getCurrentTurn().getIsAI()) {

            // inactive all buttons
            inactiveAllButtons();

            aiMove();
        }
    }

    public void onCloseGameBtn_Click(View view){
        // dialog message
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Would you like to exit this game?");
        dlgAlert.setTitle("Application Alert");
        dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // close game and go back to MAIN page
                Intent intentMain = new Intent(GameActivity.this, MainActivity.class);
                intentMain.putExtra(EXTRA_MESSAGE, "Go back to Main menu");
                GameActivity.this.startActivity(intentMain);
            }
        });
        dlgAlert.setNegativeButton("No", null);
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    public void onVPInfo_Click(View view){
        TextView txt = (TextView) findViewById(view.getId());
        String nation = (String) txt.getText();

        // open nation info.
        openNationInfo(nation);
    }

    public void onImgVPInfo_Click(View view){
        ImageView img = (ImageView) findViewById(view.getId());
        String nation = (String) img.getContentDescription();

        // open nation info.
        openNationInfo(nation);
    }

    public void onFlagInfo_Click(View view){
        String nation = this.board.getCurrentTurn().getCardSelected().getName();

        // open nation info.
        openNationInfo(nation);
    }

    private void openNationInfo(String nation){
        Intent intentInfo = new Intent(GameActivity.this, NationInfoActivity.class);
        intentInfo.putExtra(EXTRA_MESSAGE, nation);
        GameActivity.this.startActivity(intentInfo);
    }

    public void onChangeDisplayBtn_Click(View view){
        this.map = !this.map;
        displayMap();
    }

    public void onDiceOpportunities_Click(View view){
        // open Diplomatic Attack Table.
        Intent intentInfo = new Intent(GameActivity.this, DiplomaticAttack.class);
        GameActivity.this.startActivity(intentInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCOde, Intent data) {
        super.onActivityResult(requestCode, resultCOde, data);
        if (requestCode == 0) {   // 0 is restart button clicked

            game.startGame();
            displayPlayer();
            displayAction();

            activeAllButtons();
            displayNumPFByNations();
            updateNumPF();

            clearHistory();

            if (this.board.getCurrentTurn().getIsAI()) {

                // inactive all buttons
                inactiveAllButtons();

                aiMove();
            }
        }
        else if (requestCode == 1){     // 1 is action activity
            if (board.getCurrentAction().equals("PlacePF") && game.getPlayer(0).getTotalTurn() > 10){
                setGameFinish();
                checkGameFinish();
            }

            if (board.getCurrentAction().equals("OpportunityAttack")){
                // show Attack result popup
                Intent intentAttack = new Intent(GameActivity.this, OpportunityAttackActivity.class);
                intentAttack.putExtra(EXTRA_MESSAGE, this.board.getNations(this.nationSelected).getName());
                GameActivity.this.startActivityForResult(intentAttack, 4);     // 4 is opportunity attacking
            }

            displayPlayer();
            displayNumPFByNations();
            updateNumPF();

            // disable the nation button that was clicked
            if (this.board.getCurrentAction().equals("PlacePF")) {
                if (this.game.getLastAction().equals("ConfirmPlacePF"))
                    disableButton(this.btnNation[this.nationSelected]);
            }

            if (resultCOde == RESULT_OK) {
                String location = this.board.getNations(this.nationSelected).getName();
                int pf = Integer.parseInt(data.getExtras().getString("pf"));

                if (pf > 0)
                    addHistory(location, pf);
            }

            displayAction();
        }
        else if (requestCode == 2){     // 2 is attacking pop-up
            setGameFinish();
            checkGameFinish();

            displayPlayer();
            displayAction();
            updateNumPF();

            if (this.board.getCurrentTurn().getIsAI()) {
                displayPlayer();
                displayAction();

                // inactive all buttons
                inactiveAllButtons();

                aiMove();
            }
        }
        else if (requestCode == 3){     // 3 is AI actions summary
            activeAllButtons();

            setGameFinish();
            checkGameFinish();

            displayPlayer();
            displayAction();
            enableAllButton();
            displayNumPFByNations();
            updateNumPF();

            clearHistory();

            if (this.board.getCurrentTurn().getIsAI()) {

                // inactive all buttons
                inactiveAllButtons();

                if (!this.game.getGameFinish())
                    aiMove();
            }
        }
        else if (requestCode == 4){     // 4 is opportunity attacking
            if (board.getCurrentAction().equals("AttackConfirm")) {
                this.board.getCurrentTurn().attack(this.board.getNations(this.nationSelected), this.board.getCurrentDef());

                // show Attack result popup
                Intent intentAttack = new Intent(GameActivity.this, AttackActivity.class);
                intentAttack.putExtra(EXTRA_MESSAGE, this.board.getNations(this.nationSelected).getName());
                GameActivity.this.startActivityForResult(intentAttack, 2);     // 2 is attacking result

                clearHistory();
            }
        }
    }

    private void aiMove(){
        new AiMove().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class AiMove extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display waiting pop-up.
            dialog = new ProgressDialog(GameActivity.this);
            dialog.setTitle("");
            dialog.setMessage("AI processing");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            /** AI activities **/
                Board board = Board.getInstance();
                Players currentPlayer = board.getCurrentTurn();

                Actions currentAction;
                // check AI level
            switch (currentPlayer.getDifficulty()) {
                case "Very Easy":   //  Very Easy level
                    currentAction = currentPlayer.autoAction1(board);
                    break;
                case "Easy":        //  Easy level
                    currentAction = currentPlayer.autoAction2(board, 1, 11, false);
                    break;
                case "Medium":      //  Medium level
                    currentAction = currentPlayer.autoAction2(board, 6, 3, false);
                    break;
                default:            //  Hard level
                    currentAction = currentPlayer.autoAction2(board, 11, 2, false);
                    break;
            }

                // remember the AI actions
                board.setCurrentAIActions(currentAction);

                // AI placing PF
                for (Map.Entry<Nations, Integer> entry : currentAction.getPlacePF().entrySet()){
                    currentPlayer.placePF(entry.getKey().getName(), entry.getValue());
                }

                // AI attacking
                if (currentAction.getAttack().size() > 0){
                    for (Map.Entry<Nations, Players> entry : currentAction.getAttack().entrySet()){
                        // find the location
                        Nations location = null;
                        for (Nations n : board.getBoard()){
                            if (n.getName().equals(entry.getKey().getName()))
                                location = n;
                        }
                        currentPlayer.attack(location, entry.getValue());
                    }
                }

                updateNumPF();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // close the waiting pop-up
            dialog.dismiss();

            // display the result as pop-up
            Intent intentAIActions = new Intent(GameActivity.this, AiActionsActivity.class);
            GameActivity.this.startActivityForResult(intentAIActions, 3);     // 1 is click any nation or attack result popup
        }
    }
}
