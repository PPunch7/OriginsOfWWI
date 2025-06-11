package com.example.originsofworldwari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AttackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack);

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

        getWindow().setLayout((int)(width * .9), (int)(height * .43));
    }

    @SuppressLint("SetTextI18n")
    private void setAttDetails(){
        Board board = Board.getInstance();
        TextView txtLocation, txtAttacker, txtDefender, txtAttackerPF, txtDefenderPF, txtAttType, txtAttTypeDesc;
        ImageView imgDice, imgAttacker, imgDefender;
        Intent intent = getIntent();

        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtAttacker = (TextView) findViewById(R.id.txtAttacker);
        txtDefender = (TextView) findViewById(R.id.txtDefender);
        txtAttackerPF = (TextView) findViewById(R.id.txtAttackerPF);
        txtDefenderPF = (TextView) findViewById(R.id.txtDefenderPF);
        imgDice = (ImageView) findViewById(R.id.imgDice);
        txtAttType = (TextView) findViewById(R.id.txtAttType);
        txtAttTypeDesc = (TextView) findViewById(R.id.txtAttTypeDesc);
        imgAttacker = (ImageView) findViewById(R.id.imgAttacker);
        imgDefender = (ImageView) findViewById(R.id.imgDefender);

        txtLocation.setText(intent.getStringExtra(GameActivity.EXTRA_MESSAGE));
        txtAttacker.setText(board.getCurrentTurn().getCardSelected().getName());
        txtDefender.setText(board.getCurrentDef().getCardSelected().getName());
        txtAttackerPF.setText(Integer.toString(board.getCurrentAttPF()));
        txtDefenderPF.setText(Integer.toString(board.getCurrentDefPF()));
        imgDice.setImageDrawable(displayNumDice(board.getCurrentNumDice()));
        txtAttType.setText(descAttType(board.getCurrentAttType()));
        if (txtAttType.getText().equals("Attacker Eliminated"))
            txtAttType.setTextColor(Color.parseColor("#FF0000"));
        else if(txtAttType.getText().equals("Defender Eliminated"))
            txtAttType.setTextColor(Color.parseColor("#36A100"));
        txtAttTypeDesc.setText(descAttTypeDesc(board.getCurrentAttType()));
        imgAttacker.setImageDrawable(chooseNationFlag(board.getCurrentTurn().getCardSelected().getName()));
        imgDefender.setImageDrawable(chooseNationFlag(board.getCurrentDef().getCardSelected().getName()));
    }

    private String descAttType(String attType){
        if (attType.equals("AE"))
            return getResources().getString(R.string.ae);
        else if (attType.equals("EX"))
            return getResources().getString(R.string.ex);
        else
            return getResources().getString(R.string.de);
    }

    private String descAttTypeDesc(String attType){
        if (attType.equals("AE"))
            return "The attacker's PF's are eliminated, the defender does not lose any.";
        else if (attType.equals("EX"))
            return "The lesser number of PF's loses all of them, the other loses an equal amount.";
        else
            return "The defender's PF's are eliminated, the attacker does not lose any.";
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
