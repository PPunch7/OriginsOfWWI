package com.example.originsofworldwari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class OpportunityAttackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity_attack);

        // size layout
        setLayoutPopup();

        // set details
        setAttDetails();
    }

    private void setLayoutPopup(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .9), (int)(height * .5));
    }

    @SuppressLint("SetTextI18n")
    private void setAttDetails(){
        Board board = Board.getInstance();
        TextView txtLocation, txtAttacker, txtDefender, txtAttackerPF, txtDefenderPF;
        TableLayout tblOpportunity = (TableLayout) findViewById(R.id.tblOpportunity);
        tblOpportunity.setColumnStretchable(0, true);
        tblOpportunity.setColumnStretchable(1, true);
        tblOpportunity.setColumnStretchable(2, true);
        ImageView imgAttacker, imgDefender;
        Intent intent = getIntent();

        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtAttacker = (TextView) findViewById(R.id.txtAttacker);
        txtDefender = (TextView) findViewById(R.id.txtDefender);
        txtAttackerPF = (TextView) findViewById(R.id.txtAttackerPF);
        txtDefenderPF = (TextView) findViewById(R.id.txtDefenderPF);
        imgAttacker = (ImageView) findViewById(R.id.imgAttacker);
        imgDefender = (ImageView) findViewById(R.id.imgDefender);

        // finding location
        Nations location = null;

        for (Nations n : board.getBoard()){
            if (n.getName().equals(intent.getStringExtra(GameActivity.EXTRA_MESSAGE))) {
                location = n;
                break;
            }
        }

        txtLocation.setText(intent.getStringExtra(GameActivity.EXTRA_MESSAGE));
        txtAttacker.setText(board.getCurrentTurn().getCardSelected().getName());
        txtDefender.setText(board.getCurrentDef().getCardSelected().getName());
        txtAttackerPF.setText(Integer.toString(location.getPF(board.getCurrentTurn())));
        txtDefenderPF.setText(Integer.toString(location.getPF(board.getCurrentDef())));
        imgAttacker.setImageDrawable(chooseNationFlag(board.getCurrentTurn().getCardSelected().getName()));
        imgDefender.setImageDrawable(chooseNationFlag(board.getCurrentDef().getCardSelected().getName()));

        // finding players and their PFs
        int attPF, defPF;

        if (location != null) {
            attPF = location.getPF(board.getCurrentTurn());
            defPF = location.getPF(board.getCurrentDef());
            ImageView imgDice1, imgDice2, imgDice3, imgDice4, imgDice5, imgDice6;
            ArrayList<ImageView> diceList = new ArrayList<ImageView>();
            imgDice1 = new ImageView(this);
            imgDice1.setImageResource(R.drawable.dice1);
            setImageDiceStyle(imgDice1);
            imgDice2 = new ImageView(this);
            imgDice2.setImageResource(R.drawable.dice2);
            setImageDiceStyle(imgDice2);
            imgDice3 = new ImageView(this);
            imgDice3.setImageResource(R.drawable.dice3);
            setImageDiceStyle(imgDice3);
            imgDice4 = new ImageView(this);
            imgDice4.setImageResource(R.drawable.dice4);
            setImageDiceStyle(imgDice4);
            imgDice5 = new ImageView(this);
            imgDice5.setImageResource(R.drawable.dice5);
            setImageDiceStyle(imgDice5);
            imgDice6 = new ImageView(this);
            imgDice6.setImageResource(R.drawable.dice6);
            setImageDiceStyle(imgDice6);

            // adding all opportunities
            if ((attPF/defPF) >= 4){
                diceList.clear();
                diceList.add(imgDice1);
                diceList.add(imgDice2);
                diceList.add(imgDice3);
                tblOpportunity.addView(newRow(getResources().getString(R.string.ex), "50%", diceList));

                diceList.clear();
                diceList.add(imgDice4);
                diceList.add(imgDice5);
                diceList.add(imgDice6);
                tblOpportunity.addView(newRow(getResources().getString(R.string.de), "50%", diceList));
            }
            else if ((attPF/defPF) >= 3){
                diceList.clear();
                diceList.add(imgDice1);
                diceList.add(imgDice2);
                diceList.add(imgDice3);
                diceList.add(imgDice4);
                tblOpportunity.addView(newRow(getResources().getString(R.string.ex), "67%", diceList));

                diceList.clear();
                diceList.add(imgDice5);
                diceList.add(imgDice6);
                tblOpportunity.addView(newRow(getResources().getString(R.string.de), "33%", diceList));
            }
            else if ((attPF/defPF) >= 2){
                diceList.clear();
                diceList.add(imgDice1);
                tblOpportunity.addView(newRow(getResources().getString(R.string.ae), "17%", diceList));

                diceList.clear();
                diceList.add(imgDice2);
                diceList.add(imgDice3);
                diceList.add(imgDice4);
                diceList.add(imgDice5);
                tblOpportunity.addView(newRow(getResources().getString(R.string.ex), "66%", diceList));

                diceList.clear();
                diceList.add(imgDice6);
                tblOpportunity.addView(newRow(getResources().getString(R.string.de), "17%", diceList));
            }
            else if ((attPF/defPF) >= 1){
                diceList.clear();
                diceList.add(imgDice1);
                diceList.add(imgDice2);
                tblOpportunity.addView(newRow(getResources().getString(R.string.ae), "33%", diceList));

                diceList.clear();
                diceList.add(imgDice3);
                diceList.add(imgDice4);
                diceList.add(imgDice5);
                tblOpportunity.addView(newRow(getResources().getString(R.string.ex), "50%", diceList));

                diceList.clear();
                diceList.add(imgDice6);
                tblOpportunity.addView(newRow(getResources().getString(R.string.de), "17%", diceList));
            }
            else {
                diceList.clear();
                diceList.add(imgDice1);
                diceList.add(imgDice2);
                diceList.add(imgDice3);
                tblOpportunity.addView(newRow(getResources().getString(R.string.ae), "50%", diceList));

                diceList.clear();
                diceList.add(imgDice4);
                diceList.add(imgDice5);
                diceList.add(imgDice6);
                tblOpportunity.addView(newRow(getResources().getString(R.string.ex), "50%", diceList));
            }
        }
    }

    private void setImageDiceStyle(ImageView img){
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(42, 42);
        img.setLayoutParams(lp);
        img.setPadding(2, 0, 2, 0);
    }

    private TableRow newRow(String oppor, String percent, ArrayList<ImageView> dice){
        TableRow tr = new TableRow(this);
        TextView txtOppor, txtPercent;
        txtOppor = new TextView(this);
        txtOppor.setText(oppor);
        if (oppor.equals("Attacker Eliminated"))
            txtOppor.setTextColor(Color.parseColor("#FF0000"));
        else if(oppor.equals("Defender Eliminated"))
            txtOppor.setTextColor(Color.parseColor("#36A100"));
        txtOppor.setTextSize(20);
        txtOppor.setGravity(Gravity.CENTER);
        txtPercent = new TextView(this);
        txtPercent.setText(percent);
        txtPercent.setTextSize(20);
        txtPercent.setGravity(Gravity.CENTER);
        LinearLayout lnlDice = new LinearLayout(this);
        for (int i=0; i<dice.size(); i++){
            lnlDice.addView(dice.get(i));
        }
        lnlDice.setGravity(Gravity.CENTER);

        tr.addView(txtOppor);
        tr.addView(txtPercent);
        tr.addView(lnlDice);

        return tr;
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
        Board.getInstance().setCurrentAction("Attack");
        finish();
    }

    public void onRollBtn_Click(View view){
        Board.getInstance().setCurrentAction("AttackConfirm");
        Game.getInstance().setLastAction("ConfirmAttack");

        finish();
    }
}
