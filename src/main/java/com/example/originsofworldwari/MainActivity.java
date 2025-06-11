package com.example.originsofworldwari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "";
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = Game.getInstance();
        game.clearGame();
    }

    public void onPlayerBtn_Click(View view){
        game.setMode("withPlayers");

        Intent intentSelectNations = new Intent(MainActivity.this, SelectNationsActivity.class);
        intentSelectNations.putExtra(EXTRA_MESSAGE, "Play with player!!");
        MainActivity.this.startActivity(intentSelectNations);
    }

    public void onAIBtn_Click(View view){
        game.setMode("withAI");

        Intent intentSelectNations = new Intent(MainActivity.this, SelectNationsActivity.class);
        intentSelectNations.putExtra(EXTRA_MESSAGE, "Play with player!!");
        MainActivity.this.startActivity(intentSelectNations);
    }

    public void onRulesBtn_Click(View view){
        Intent intentInformation = new Intent(MainActivity.this, InformationActivity.class);
        intentInformation.putExtra(EXTRA_MESSAGE, "Rules");
        MainActivity.this.startActivity(intentInformation);
    }

    public void onHowToPlayBtn_Click(View view){
        Intent intentInformation = new Intent(MainActivity.this, InformationActivity.class);
        intentInformation.putExtra(EXTRA_MESSAGE, "How to play");
        MainActivity.this.startActivity(intentInformation);
    }
}
