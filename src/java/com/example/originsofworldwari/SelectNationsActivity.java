package com.example.originsofworldwari;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SelectNationsActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "";
    private Game game;
    private Players[] players = new Players[5];
    private int[] playerType = new int[5];  // 0: human, 1: ai
    private boolean[] nationsSelected = new boolean[5];     // for trigger selection
    private int[] txtViewPlayers = {
            R.id.txtPlayer1,
            R.id.txtPlayer2,
            R.id.txtPlayer3,
            R.id.txtPlayer4,
            R.id.txtPlayer5
    };
    private TextView[] lblViewPlayers = new TextView[5];
    private Spinner[] spnPlayers = new Spinner[4];
    private int[] iBtnNations = {
            R.id.ibtnBritainCard,
            R.id.ibtnFranceCard,
            R.id.ibtnGermanyCard,
            R.id.ibtnRussiaCard,
            R.id.ibtnAustriaCard
    };
    private int[] spc = {
            R.id.spcEnd2,
            R.id.spcEnd3,
            R.id.spcEnd4,
            R.id.spcEnd5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_nations);

        game = Game.getInstance();

        addDropDownList();
        addCardInGame();
        setPlayers();
    }

    private void addDropDownList(){
        spnPlayers[0] = (Spinner) findViewById(R.id.spnPlayer2);
        spnPlayers[1] = (Spinner) findViewById(R.id.spnPlayer3);
        spnPlayers[2] = (Spinner) findViewById(R.id.spnPlayer4);
        spnPlayers[3] = (Spinner) findViewById(R.id.spnPlayer5);

        ArrayAdapter<String> lvlAdp = new ArrayAdapter<String>(this,
                R.layout.spinner_item, getResources().getStringArray(R.array.difficult));
        for (Spinner s : spnPlayers)
            s.setAdapter(lvlAdp);
    }

    private void addCardInGame(){
        game.addCard(createCard(0, "Britain", 14));
        game.addCard(createCard(1, "France", 12));
        game.addCard(createCard(2, "Germany", 16));
        game.addCard(createCard(3, "Russia", 10));
        game.addCard(createCard(4, "Austria", 10));
    }

    private Cards createCard(int id, String name, int pf){
        return new Cards(id, name, pf);
    }

    private void setPlayers(){
        lblViewPlayers[0] = (TextView) findViewById(R.id.lblPlayer1);
        lblViewPlayers[1] = (TextView) findViewById(R.id.lblPlayer2);
        lblViewPlayers[2] = (TextView) findViewById(R.id.lblPlayer3);
        lblViewPlayers[3] = (TextView) findViewById(R.id.lblPlayer4);
        lblViewPlayers[4] = (TextView) findViewById(R.id.lblPlayer5);
        if (game.getMode().equals("withPlayers")){
            lblViewPlayers[0].setText(R.string.player1);
            players[0] = new Players(0, lblViewPlayers[0].getText().toString(), "green", false);
            playerType[0] = 0;
            lblViewPlayers[1].setText(R.string.player2);
            players[1] = new Players(1, lblViewPlayers[1].getText().toString(), "blue", false);
            playerType[1] = 0;
            lblViewPlayers[2].setText(R.string.player3);
            players[2] = new Players(2, lblViewPlayers[2].getText().toString(), "orange", false);
            playerType[2] = 0;
            lblViewPlayers[3].setText(R.string.player4);
            players[3] = new Players(3, lblViewPlayers[3].getText().toString(), "pink", false);
            playerType[3] = 0;
            lblViewPlayers[4].setText(R.string.player5);
            players[4] = new Players(4, lblViewPlayers[4].getText().toString(), "black", false);
            playerType[4] = 0;
            hideIconBtn();
            hideAllSpinner();
        }
        else{
            lblViewPlayers[0].setText(R.string.player1);
            players[0] = new Players(0, lblViewPlayers[0].getText().toString(), "green", false);
            playerType[0] = 0;
            lblViewPlayers[1].setText(R.string.ai1);
            players[1] = new Players(1, lblViewPlayers[1].getText().toString(), "blue", true);
            playerType[1] = 1;
            lblViewPlayers[2].setText(R.string.ai2);
            players[2] = new Players(2, lblViewPlayers[2].getText().toString(), "orange", true);
            playerType[2] = 1;
            lblViewPlayers[3].setText(R.string.ai3);
            players[3] = new Players(3, lblViewPlayers[3].getText().toString(), "pink", true);
            playerType[3] = 1;
            lblViewPlayers[4].setText(R.string.ai4);
            players[4] = new Players(4, lblViewPlayers[4].getText().toString(), "black", true);
            playerType[4] = 1;
        }
    }

    private void setNationsDisplay(TextView txtView, String nation){
        txtView.setText(nation);
    }

    private void addPlayerInGame(){
        // set AI difficulty
        for (int i=0; i<this.playerType.length; i++){
            if (playerType[i] == 1){        // the player is AI
                players[i].setDifficulty(spnPlayers[i - 1].getSelectedItem().toString());
            }
        }

        // adding players
        for (int i=0; i<this.players.length; i++){
            Players playerInOrder = null;
            for (Players p : this.players){
                if (p.getCardSelected().getId() == i) {
                    playerInOrder = p;
                    break;
                }
            }
            if (playerInOrder != null)
                this.game.addPlayer(playerInOrder);
        }
    }

    private void hideIconBtn(){
        ImageButton[] iBtn = new ImageButton[4];
        iBtn[0] = (ImageButton) findViewById(R.id.ibtnPlayer2);
        iBtn[1] = (ImageButton) findViewById(R.id.ibtnPlayer3);
        iBtn[2] = (ImageButton) findViewById(R.id.ibtnPlayer4);
        iBtn[3] = (ImageButton) findViewById(R.id.ibtnPlayer5);
        for (ImageButton btn : iBtn)
            btn.setVisibility(View.GONE);

        // adjust space (HEAD location)
        Space[] spc = new Space[4];
        spc[0] = (Space) findViewById(R.id.spcHead2);
        spc[1] = (Space) findViewById(R.id.spcHead3);
        spc[2] = (Space) findViewById(R.id.spcHead4);
        spc[3] = (Space) findViewById(R.id.spcHead5);
        for (Space s : spc)
            setWeightSpace(s, 2.0f);
    }

    private void hideAllSpinner(){
        for (Spinner s : spnPlayers)
            hideSpinner(s);

        // adjust space (END location)
        for (int s : spc)
            setWeightSpace((Space) findViewById(s), 4.0f);
    }

    private void hideSpinner(Spinner s){
        s.setVisibility(View.GONE);
    }

    private void showSpinner(Spinner s){
        s.setVisibility(View.VISIBLE);
    }

    private void setWeightSpace(Space spc, float weight){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, weight);
        spc.setLayoutParams(params);
    }

    private void displayNationBtn(ImageButton iBtn, int numPlayer){
        if (numPlayer == -1)
            iBtn.setBackground(getResources().getDrawable(R.drawable.border));
        else if (numPlayer == 0)
            iBtn.setBackground(getResources().getDrawable(R.drawable.border_green));
        else if (numPlayer == 1)
            iBtn.setBackground(getResources().getDrawable(R.drawable.border_blue));
        else if (numPlayer == 2)
            iBtn.setBackground(getResources().getDrawable(R.drawable.border_orange));
        else if (numPlayer == 3)
            iBtn.setBackground(getResources().getDrawable(R.drawable.border_pink));
        else if (numPlayer == 4)
            iBtn.setBackground(getResources().getDrawable(R.drawable.border_black));
    }

    public void onSelectNationsBtn_Click(View view){
        ImageButton btn = (ImageButton) findViewById(view.getId());
        int numNation = Integer.parseInt(btn.getContentDescription().toString());
        TextView txtView;

        if (!(nationsSelected[numNation])){    // this nation has not selected yet
            // find the first player who has not selected nation
            for (int i=0; i<players.length; i++){
                if (players[i].getCardSelected() == null){
                    // set this nation for this player
                    players[i].selectCard(this.game.getCard(numNation));
                    txtView = (TextView) findViewById(txtViewPlayers[i]);
                    setNationsDisplay(txtView, this.game.getCard(numNation).getName());

                    nationsSelected[numNation] = true;
                    displayNationBtn(btn, i);

                    break;
                }
            }
        }
        else{                               // this nation has already selected
            // find the player who has selected this nation
            for (int i=0; i<players.length; i++){
                if (players[i].getCardSelected() != null) {
                    if (players[i].getCardSelected().getId() == numNation) {
                        // unselected this nation
                        players[i].selectCard(null);
                        txtView = (TextView) findViewById(txtViewPlayers[i]);
                        setNationsDisplay(txtView, "None");

                        nationsSelected[numNation] = false;
                        displayNationBtn(btn, -1);

                        break;
                    }
                }
            }
        }
    }

    public void onIconBtn_Click(View view){
        ImageButton btn = (ImageButton) findViewById(view.getId());
        int index = Integer.parseInt(btn.getContentDescription().toString());
        if (this.playerType[index] == 0) {
            btn.setBackground(getResources().getDrawable(R.drawable.computer_icon));
            this.playerType[index] = 1;
            showSpinner(spnPlayers[index - 1]);
            setWeightSpace((Space) findViewById(spc[index-1]), 1.0f);
        }
        else {
            btn.setBackground(getResources().getDrawable(R.drawable.human_icon));
            this.playerType[index] = 0;
            hideSpinner(spnPlayers[index - 1]);
            setWeightSpace((Space) findViewById(spc[index-1]), 4.0f);
        }
        setPlayerName();
    }

    @SuppressLint("SetTextI18n")
    private void setPlayerName(){
        int numPlayer = 0;
        int numAI = 0;
        for (int i=0; i<this.playerType.length; i ++){
            if (this.playerType[i] == 0){
                numPlayer += 1;
                this.lblViewPlayers[i].setText("Player0" + numPlayer);
                this.players[i].setIsAI(false);
            }
            else{
                numAI += 1;
                this.lblViewPlayers[i].setText("AI0" + numAI);
                this.players[i].setIsAI(true);
            }
            this.players[i].setName(this.lblViewPlayers[i].getText().toString());
        }
    }

    private void randomNation(Players player){
        // get nations that have not selected yet
        ArrayList<Integer> nationsList = new ArrayList<Integer>();
        for (int i=0; i<nationsSelected.length; i++){
            if (!(nationsSelected[i]))
                nationsList.add(i);
        }

        // random the nation from the list and set for this player
        Random r = new Random();
        int nationRandom = nationsList.get(r.nextInt(nationsList.size()));
        player.selectCard(this.game.getCard(nationRandom));
        TextView txtView = (TextView) findViewById(txtViewPlayers[player.getId()]);
        setNationsDisplay(txtView, this.game.getCard(nationRandom).getName());

        nationsSelected[nationRandom] = true;
        displayNationBtn((ImageButton) findViewById(iBtnNations[nationRandom]), player.getId());
    }

    private int countPlayerUnselected(){
        int num = 0;
        for (Players p : players) {
            if (p.getCardSelected() == null)
                num++;
        }

        return num;
    }

    public void onRandomBtn_Click(View view){
        // get number of players who has not selected any nation
        int num = countPlayerUnselected();

        if (num > 0) {  // if at least one player has not selected nation yet
            for (Players p : players) {
                if (p.getCardSelected() == null)
                    randomNation(p);
            }
        }
        else {          // if all players have already selected nation
            // clear nations selected
            Arrays.fill(nationsSelected, false);

            // re-random all player
            for (Players p : players) {
                p.selectCard(null);
                randomNation(p);
            }
        }
    }

    public void onBackBtn_Click(View view){
        Intent intentMain = new Intent(SelectNationsActivity.this, MainActivity.class);
        intentMain.putExtra(EXTRA_MESSAGE, "Go back to Main");
        SelectNationsActivity.this.startActivity(intentMain);
    }

    public void onNextBtn_Click(View view){
        if (countPlayerUnselected() > 0){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("All players have to select nation.");
            dlgAlert.setTitle("Application Alert");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
        else {
            addPlayerInGame();

            Intent intentGame = new Intent(SelectNationsActivity.this, GameActivity.class);
            intentGame.putExtra(EXTRA_MESSAGE, "Go to Game");
            SelectNationsActivity.this.startActivity(intentGame);
        }
    }
}
