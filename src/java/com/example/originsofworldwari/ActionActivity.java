package com.example.originsofworldwari;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActionActivity extends AppCompatActivity {

    private Game game;
    private Board board;
    private SeekBar seekBarPF;
    private TextView txtViewPF;
    private String nationDef = "";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        this.game = Game.getInstance();
        this.board = Board.getInstance();

        // size layout
        setLayoutPopup();

        // set Nation header
        setNationHeader();

        // set Action label
        setActionLabel();

        // set Numbers of PF
        setNumPF();

        // Seek button control
        loadSeekControl();

        // set Action mode
        displayActionMode();

        // display VICTORY POINT
        displayVP();
    }

    private void setLayoutPopup(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .9), (int)(height * .75));
    }

    private void setNationHeader(){
        this.intent = getIntent();
        TextView txtNation;
        txtNation = (TextView) findViewById(R.id.lblNations);
        txtNation.setText(intent.getStringExtra(GameActivity.EXTRA_MESSAGE));
    }

    private void setActionLabel(){
        TextView txtAction;
        txtAction = (TextView) findViewById(R.id.txtActions);
        txtAction.setText(this.board.getCurrentAction());
    }

    @SuppressLint("SetTextI18n")
    private void displayActionMode(){
        String currentAction = this.board.getCurrentAction();
        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        if (currentAction.equals("PlacePF")) {
            ImageButton ibtnBritain, ibtnFrance, ibtnGermany, ibtnRussia, ibtnAustria;
            ibtnBritain = (ImageButton) findViewById(R.id.ibtnBritain);
            ibtnBritain.setClickable(false);
            ibtnFrance = (ImageButton) findViewById(R.id.ibtnFrance);
            ibtnFrance.setClickable(false);
            ibtnGermany = (ImageButton) findViewById(R.id.ibtnGermany);
            ibtnGermany.setClickable(false);
            ibtnRussia = (ImageButton) findViewById(R.id.ibtnRussia);
            ibtnRussia.setClickable(false);
            ibtnAustria = (ImageButton) findViewById(R.id.ibtnAustria);
            ibtnAustria.setClickable(false);

            btnConfirm.setText("Place");
            // display PLACE button -> do not allow to place more than 1 time per turn
            displayPlaceBtn(true);
        }
        else if (currentAction.equals("Attack")){
            seekBarPF.setVisibility(View.GONE);
            txtViewPF.setVisibility(View.GONE);

            btnConfirm.setText("Attack");
            // display PLACE button -> do not allow to place more than 1 time per turn
            displayPlaceBtn(false);
        }
    }

    private void displayPlaceBtn(boolean placing) {
        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        if (placing) {
            ArrayList<HashMap<String, Integer>> historyTurns = this.game.getHistoryTurns();
            if (historyTurns != null) {
                for (HashMap<String, Integer> his : historyTurns) {
                    for (Map.Entry<String, Integer> entry : his.entrySet()) {
                        if (entry.getKey().equals(intent.getStringExtra(GameActivity.EXTRA_MESSAGE))) {
                            btnConfirm.setClickable(false);
                            btnConfirm.setEnabled(false);
                            seekBarPF.setMax(0);
                        }
                    }
                }
            }
        }
        else{
            for (Nations n : this.board.getBoard()){
                if (n.getName().equals(intent.getStringExtra(GameActivity.EXTRA_MESSAGE))) {
                    if (n.getPF(this.board.getCurrentTurn()) <= 0) {
                        btnConfirm.setClickable(false);
                        btnConfirm.setEnabled(false);
                    }
                }
            }
        }
    }

    private void displayVP(){
        String location = intent.getStringExtra(GameActivity.EXTRA_MESSAGE);
        assert location != null;
        switch (location) {
            case "Britain":
                addNationForVP("France", 2);
                break;
            case "France":
                addNationForVP("n/a", 0);
                break;
            case "Germany":
                addNationForVP("Austria", 4);
                break;
            case "Russia":
                addNationForVP("France", 3);
                addNationForVP("Germany", 2);
                break;
            case "Austria":
                addNationForVP("Germany", 4);
                break;
            case "Italy":
                addNationForVP("Britain", 3);
                addNationForVP("France", 1);
                addNationForVP("Germany", 2);
                addNationForVP("Austria", 2);
                break;
            case "Serbia":
                addNationForVP("Russia", 5);
                addNationForVP("Austria (E)", 10);
                break;
            case "Romania":
                addNationForVP("Russia", 3);
                addNationForVP("Austria", 2);
                break;
            case "Bulgaria":
                addNationForVP("Russia", 1);
                break;
            case "Greece":
                addNationForVP("Britain", 1);
                addNationForVP("Russia", 1);
                break;
            case "Turkey":
                addNationForVP("Britain", 2);
                addNationForVP("Russia (E)", 5);
                break;
            case "Far East":
                addNationForVP("Britain (E)", 4);
                addNationForVP("Russia", 3);
                break;
            case "Africa":
                addNationForVP("France (E)", 5);
                addNationForVP("Germany", 3);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void addNationForVP(String nation, int vp){
        TableLayout tblVP = (TableLayout) findViewById(R.id.tblVP);
        tblVP.setColumnStretchable(0, true);
        tblVP.setColumnStretchable(1, true);

        TableRow tr = new TableRow(this);
        TextView txtNation, txtVP;
        txtNation = new TextView(this);
        txtNation.setText(nation);
        txtNation.setTextSize(18);
        txtNation.setGravity(Gravity.CENTER);
        txtVP = new TextView(this);
        if (vp > 0)
            txtVP.setText(Integer.toString(vp));
        else
            txtVP.setText("n/a");
        txtVP.setTextSize(18);
        txtVP.setGravity(Gravity.CENTER);
        tr.addView(txtNation);
        tr.addView(txtVP);
        tblVP.addView(tr);
    }

    @SuppressLint("SetTextI18n")
    private void setNumPF(){
        Intent intent = getIntent();
        TextView txtBritainPF, txtFrancePF, txtGermanyPF, txtRussiaPF, txtAustriaPF;
        Nations currentNation = null;

        for (Nations n : this.board.getBoard()){
            if (n.getName().equals(intent.getStringExtra(GameActivity.EXTRA_MESSAGE))) {
                currentNation = n;
                break;
            }
        }

        txtBritainPF = (TextView) findViewById(R.id.txtBritainPF);
        txtFrancePF = (TextView) findViewById(R.id.txtFrancePF);
        txtGermanyPF = (TextView) findViewById(R.id.txtGermanyPF);
        txtRussiaPF = (TextView) findViewById(R.id.txtRussiaPF);
        txtAustriaPF = (TextView) findViewById(R.id.txtAustriaPF);

        if (currentNation != null) {
            displayPF(txtBritainPF, currentNation.getPF(this.game.getPlayer(0)));
            displayPF(txtFrancePF, currentNation.getPF(this.game.getPlayer(1)));
            displayPF(txtGermanyPF, currentNation.getPF(this.game.getPlayer(2)));
            displayPF(txtRussiaPF, currentNation.getPF(this.game.getPlayer(3)));
            displayPF(txtAustriaPF, currentNation.getPF(this.game.getPlayer(4)));
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayPF(TextView txt, int pf){
        txt.setText(Integer.toString(pf));
        if (pf >= 10)
            txt.setTextColor(Color.parseColor("#FF0030A3"));
        else if (pf >= 1)
            txt.setTextColor(Color.parseColor("#FF000000"));
        else
            txt.setTextColor(Color.parseColor("#8A000000"));
    }

    @SuppressLint("SetTextI18n")
    private void loadSeekControl(){
        seekBarPF = (SeekBar) findViewById(R.id.skbNumPF);
        txtViewPF = (TextView) findViewById(R.id.lblNumPF);

        // set Max PF per turn
        this.intent = getIntent();
        // unlimited PF for own location
        if (intent.getStringExtra(GameActivity.EXTRA_MESSAGE).equals(this.board.getCurrentTurn().getCardSelected().getName())){
            seekBarPF.setMax(this.board.getCurrentAvailablePF());
        }
        else {
            if (this.board.getCurrentAvailablePF() >= 5)
                seekBarPF.setMax(5);
            else
                seekBarPF.setMax(this.board.getCurrentAvailablePF());
        }

        seekBarPF.setProgress(seekBarPF.getMax());
        txtViewPF.setText(Integer.toString(seekBarPF.getProgress()));
        seekBarPF.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtViewPF.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setDefender(){
        for (int i=0; i<this.board.getNumPlayers(); i++){
            if (this.game.getPlayer(i).getCardSelected().getName().equals(nationDef)){
                this.board.setCurrentDef(this.game.getPlayer(i));
                break;
            }
        }
    }

    private void setAllNationsBorder(){
        ImageButton ibtnBritain, ibtnFrance, ibtnGermany, ibtnRussia, ibtnAustria;
        ibtnBritain = (ImageButton) findViewById(R.id.ibtnBritain);
        ibtnBritain.setBackground(getResources().getDrawable(R.drawable.border));
        ibtnFrance = (ImageButton) findViewById(R.id.ibtnFrance);
        ibtnFrance.setBackground(getResources().getDrawable(R.drawable.border));
        ibtnGermany = (ImageButton) findViewById(R.id.ibtnGermany);
        ibtnGermany.setBackground(getResources().getDrawable(R.drawable.border));
        ibtnRussia = (ImageButton) findViewById(R.id.ibtnRussia);
        ibtnRussia.setBackground(getResources().getDrawable(R.drawable.border));
        ibtnAustria = (ImageButton) findViewById(R.id.ibtnAustria);
        ibtnAustria.setBackground(getResources().getDrawable(R.drawable.border));
    }

    private void setNationSelectedBorder(ImageButton iBtn){
        iBtn.setBackground(getResources().getDrawable(R.drawable.border_red));
    }

    public void onNationsBtn_Click(View view){
        // check attacking own PF
        if (view.getContentDescription().toString().equals(this.board.getCurrentTurn().getCardSelected().getName())) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Cannot attack your own PF.");
            dlgAlert.setTitle("Application Alert");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
        else {
            // get ths location from the board
            this.intent = getIntent();
            String locationStr = this.intent.getStringExtra(GameActivity.EXTRA_MESSAGE);
            Nations location = null;
            for (Nations n : this.board.getBoard()){
                if (n.getName().equals(locationStr))
                    location = n;
            }

            String tmpLocation = this.nationDef;
            ImageButton btn = (ImageButton) findViewById(view.getId());
            this.nationDef = btn.getContentDescription().toString();

            // get the number of defender's PF from the player that was selected
            int defPF = 0;
            for (Players p : this.game.getAllPlayer()){
                if (p.getCardSelected().getName().equals(this.nationDef))
                    defPF = location.getPF(p);
            }

            if (defPF <= 0){
                this.nationDef = tmpLocation;

                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Cannot attack the player who does not have any PF on this location.");
                dlgAlert.setTitle("Application Alert");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            }
            else {
                setAllNationsBorder();
                setNationSelectedBorder(btn);
            }
        }
    }

    public void onCancelBtn_Click(View view){
        this.game.setLastAction("CancelAction");
        finish();
    }

    public void onConfirmBtn_Click(View view){
        String currentAction = this.board.getCurrentAction();

        if (currentAction.equals("Attack") && this.nationDef.equals("")){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Have to select the player who would be defender before ATTACKING.");
            dlgAlert.setTitle("Application Alert");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
        else {
            if (currentAction.equals("PlacePF")) {
                TextView lblNations = (TextView) findViewById(R.id.lblNations);
                TextView lblNumPF = (TextView) findViewById(R.id.lblNumPF);
                String location = lblNations.getText().toString();
                int numPF = Integer.parseInt(lblNumPF.getText().toString());
                int currentAvailablePF = this.board.getCurrentAvailablePF();

                this.board.getCurrentTurn().placePF(location, numPF);

                this.board.setCurrentAvailablePF(currentAvailablePF - numPF);

                if (currentAvailablePF - numPF <= 0)
                    this.board.setCurrentAction("Attack");

                this.game.setLastAction("ConfirmPlacePF");

                this.intent.putExtra("pf", Integer.toString(numPF));
            }
            else if (currentAction.equals("Attack")) {
                setDefender();
                this.board.setCurrentAction("OpportunityAttack");
                this.intent.putExtra("pf", "-1");
            }
            setResult(RESULT_OK, this.intent);
            finish();
        }
    }
}
