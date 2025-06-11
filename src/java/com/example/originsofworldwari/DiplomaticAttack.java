package com.example.originsofworldwari;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class DiplomaticAttack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diplomatic_attack);

        // size layout
        setLayoutPopup();
    }

    private void setLayoutPopup(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .9), (int)(height * .5));
    }

    public void onCloseBtn_Click(View view){
        finish();
    }
}
