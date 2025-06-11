package com.example.originsofworldwari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class EndGameActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        // size layout
        setLayoutPopup();

        displayRanking();
    }

    private void setLayoutPopup() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .9), (int)(height * .55));
    }

    @SuppressLint("SetTextI18n")
    private void displayRanking(){
        Players winner = Game.getInstance().getWinner();
        Players[] ranking = Game.getInstance().getTotalRank();
        TextView txtWinner;
        TextView txtPlayer1, txtPlayer2, txtPlayer3, txtPlayer4, txtPlayer5;
        TextView txtNation1, txtNation2, txtNation3, txtNation4, txtNation5;
        TextView txtTotalScore1, txtTotalScore2, txtTotalScore3, txtTotalScore4, txtTotalScore5;

        // Winner
        txtWinner = (TextView) findViewById(R.id.txtWinner);
        txtWinner.setText(winner.getCardSelected().getName());

        // Players name
        txtPlayer1 = (TextView) findViewById(R.id.txtPlayer1);
        txtPlayer2 = (TextView) findViewById(R.id.txtPlayer2);
        txtPlayer3 = (TextView) findViewById(R.id.txtPlayer3);
        txtPlayer4 = (TextView) findViewById(R.id.txtPlayer4);
        txtPlayer5 = (TextView) findViewById(R.id.txtPlayer5);
        txtPlayer1.setText(ranking[0].getName());
        txtPlayer2.setText(ranking[1].getName());
        txtPlayer3.setText(ranking[2].getName());
        txtPlayer4.setText(ranking[3].getName());
        txtPlayer5.setText(ranking[4].getName());

        // Nation
        txtNation1 = (TextView) findViewById(R.id.txtNation1);
        txtNation2 = (TextView) findViewById(R.id.txtNation2);
        txtNation3 = (TextView) findViewById(R.id.txtNation3);
        txtNation4 = (TextView) findViewById(R.id.txtNation4);
        txtNation5 = (TextView) findViewById(R.id.txtNation5);
        txtNation1.setText(ranking[0].getCardSelected().getName());
        txtNation2.setText(ranking[1].getCardSelected().getName());
        txtNation3.setText(ranking[2].getCardSelected().getName());
        txtNation4.setText(ranking[3].getCardSelected().getName());
        txtNation5.setText(ranking[4].getCardSelected().getName());

        // Total score
        txtTotalScore1 = (TextView) findViewById(R.id.txtTotalScore1);
        txtTotalScore2 = (TextView) findViewById(R.id.txtTotalScore2);
        txtTotalScore3 = (TextView) findViewById(R.id.txtTotalScore3);
        txtTotalScore4 = (TextView) findViewById(R.id.txtTotalScore4);
        txtTotalScore5 = (TextView) findViewById(R.id.txtTotalScore5);
        txtTotalScore1.setText(Integer.toString(ranking[0].getTotalScore()));
        txtTotalScore2.setText(Integer.toString(ranking[1].getTotalScore()));
        txtTotalScore3.setText(Integer.toString(ranking[2].getTotalScore()));
        txtTotalScore4.setText(Integer.toString(ranking[3].getTotalScore()));
        txtTotalScore5.setText(Integer.toString(ranking[4].getTotalScore()));
    }

    public void onRestartBtn_Click(View view){
        setResult(0);
        finish();
    }

    public void onNewGameBtn_Click(View view){
        Intent intentMain = new Intent(EndGameActivity.this, MainActivity.class);
        intentMain.putExtra(EXTRA_MESSAGE, "New Game");
        EndGameActivity.this.startActivity(intentMain);

        Log.i("End Game", "New game button clicked");
    }
}
