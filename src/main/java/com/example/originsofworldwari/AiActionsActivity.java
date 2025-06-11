package com.example.originsofworldwari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class AiActionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_actions);

        // size layout
        setLayoutPopup();

        // set details
        setAllDetails();
    }

    private void setLayoutPopup(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .9), (int)(height * .75));
    }

    @SuppressLint("SetTextI18n")
    private void setAllDetails(){
        Board board = Board.getInstance();
        Actions aiActions = board.getCurrentAIActions();
        HashMap<Nations, Integer> currentPlacing = aiActions.getPlacePF();
        HashMap<Nations, Players> currentAttacking = aiActions.getAttack();

        TableLayout tblPlacingPF = (TableLayout) findViewById(R.id.tblPlacingPF);
        tblPlacingPF.setColumnStretchable(0, true);
        tblPlacingPF.setColumnStretchable(1, true);
        TextView txtPlayer, txtNation, txtAttLocation, txtDefender, txtNumDice, txtAttResult, txtAttBF, txtAttAF, txtDefBF, txtDefAF, lblResultAtt, lblResultDef;
        ImageView imgNation, imgDice;

        txtPlayer = (TextView) findViewById(R.id.txtPlayer);
        txtNation = (TextView) findViewById(R.id.txtNation);
        imgNation = (ImageView) findViewById(R.id.imgNation);
        txtAttLocation = (TextView) findViewById(R.id.txtAttLocation);
        txtDefender = (TextView) findViewById(R.id.txtDefender);
        imgDice = (ImageView) findViewById(R.id.imgDice);
        txtNumDice = (TextView) findViewById(R.id.txtNumDice);
        txtAttResult = (TextView) findViewById(R.id.txtAttResult);
        txtAttBF = (TextView) findViewById(R.id.txtAttBF);
        txtAttAF = (TextView) findViewById(R.id.txtAttAF);
        txtDefBF = (TextView) findViewById(R.id.txtDefBF);
        txtDefAF = (TextView) findViewById(R.id.txtDefAF);
        lblResultAtt = (TextView) findViewById(R.id.lblResultAtt);
        lblResultDef = (TextView) findViewById(R.id.lblResultDef);

        txtPlayer.setText(board.getCurrentTurn().getName());
        txtNation.setText(board.getCurrentTurn().getCardSelected().getName());
        imgNation.setImageDrawable(chooseNationFlag(board.getCurrentTurn().getCardSelected().getName()));

        // add the nations that was placed PF
        for (Map.Entry<Nations, Integer> entry : currentPlacing.entrySet()){
            TableRow tr = new TableRow(this);
            TextView txtLocation, txtPF;
            txtLocation = new TextView(this);
            if ((board.getCurrentTurn().getCardSelected().getName().equals("Britain") && entry.getKey().getName().equals("Far East"))
                    || (board.getCurrentTurn().getCardSelected().getName().equals("France") && entry.getKey().getName().equals("Africa"))
                    || (board.getCurrentTurn().getCardSelected().getName().equals("Russia") && entry.getKey().getName().equals("Turkey"))
                    || (board.getCurrentTurn().getCardSelected().getName().equals("Austria") && entry.getKey().getName().equals("Serbia")))
                txtLocation.setText(entry.getKey().getName() + " (E)");
            else
                txtLocation.setText(entry.getKey().getName());
            txtLocation.setTextSize(18);
            txtLocation.setGravity(Gravity.CENTER);
            txtPF = new TextView(this);
            txtPF.setText(Integer.toString(entry.getValue()));
            txtPF.setTextSize(18);
            txtPF.setGravity(Gravity.CENTER);
            tr.addView(txtLocation);
            tr.addView(txtPF);
            tblPlacingPF.addView(tr);
        }

        // display attacking
        if (currentAttacking.size() > 0){
            String attLocation = "";
            Players defender = null;
            for (Map.Entry<Nations, Players> entry : currentAttacking.entrySet()){
                attLocation = entry.getKey().getName();
                txtAttLocation.setText(attLocation);
                defender = entry.getValue();
                txtDefender.setText(defender.getCardSelected().getName());
            }
            imgDice.setImageDrawable(displayNumDice(board.getCurrentNumDice()));
            txtNumDice.setVisibility(View.GONE);
            txtAttResult.setText(descAttType(board.getCurrentAttType()));
            if (txtAttResult.getText().equals("Attacker Eliminated"))
                txtAttResult.setTextColor(Color.parseColor("#FF0000"));
            else if(txtAttResult.getText().equals("Defender Eliminated"))
                txtAttResult.setTextColor(Color.parseColor("#36A100"));
            txtAttBF.setText(Integer.toString(board.getCurrentAttPF()));
            txtAttAF.setText(Integer.toString(findPFAfterAttacking(attLocation, board.getCurrentTurn())));
            txtDefBF.setText(Integer.toString(board.getCurrentDefPF()));
            txtDefAF.setText(Integer.toString(findPFAfterAttacking(attLocation, defender)));
            lblResultAtt.setText("Attacker: " + board.getCurrentTurn().getCardSelected().getName());
            lblResultDef.setText("Defender: " + board.getCurrentDef().getCardSelected().getName());
        }
        else {
            txtAttLocation.setText("n/a");
            txtDefender.setText("n/a");
            imgDice.setVisibility(View.GONE);
            txtNumDice.setText("n/a");
            txtAttResult.setText("n/a");
            txtAttBF.setText("n/a");
            txtAttAF.setText("n/a");
            txtDefBF.setText("n/a");
            txtDefAF.setText("n/a");
        }
    }

    private int findPFAfterAttacking(String location, Players player){
        int pf = 0;
        Nations[] currentBoard = Board.getInstance().getBoard();
        for (Nations n : currentBoard){
            if (n.getName().equals(location)){
                pf = n.getPF(player);
                break;
            }
        }
        return pf;
    }

    private String descAttType(String attType){
        if (attType.equals("AE"))
            return getResources().getString(R.string.ae);
        else if (attType.equals("EX"))
            return getResources().getString(R.string.ex);
        else
            return getResources().getString(R.string.de);
    }

    private Drawable displayNumDice(int numDice){
        if (numDice == 1)
            return  getResources().getDrawable(R.drawable.dice1);
        else if (numDice == 2)
            return  getResources().getDrawable(R.drawable.dice2);
        else if (numDice == 3)
            return  getResources().getDrawable(R.drawable.dice3);
        else if (numDice == 4)
            return  getResources().getDrawable(R.drawable.dice4);
        else if (numDice == 5)
            return  getResources().getDrawable(R.drawable.dice5);
        else
            return  getResources().getDrawable(R.drawable.dice6);
    }

    private Drawable chooseNationFlag(String nation){
        switch (nation) {
            case "Britain":
                return getResources().getDrawable(R.drawable.flag_of_the_united_kingdom);
            case "France":
                return getResources().getDrawable(R.drawable.flag_of_france);
            case "Germany":
                return getResources().getDrawable(R.drawable.flag_of_germany);
            case "Russia":
                return getResources().getDrawable(R.drawable.flag_of_russia);
            default:
                return getResources().getDrawable(R.drawable.flag_of_austria_hungary);
        }
    }

    public void onCloseBtn_Click(View view){
        finish();
    }
}
