package com.example.originsofworldwari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class NationInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nation_info);

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

        getWindow().setLayout((int)(width * .9), (int)(height * .5));
    }

    private void setAllDetails(){
        Intent intent = getIntent();
        String nation = intent.getStringExtra(GameActivity.EXTRA_MESSAGE);
        TextView lblHeader = (TextView) findViewById(R.id.lblHeader);
        ImageView imgNation = (ImageView) findViewById(R.id.imgNation);

        lblHeader.setText(nation);
        assert nation != null;
        imgNation.setImageDrawable(chooseNationFlag(nation));

        switch (nation) {
            case "Britain":
                addNationForVP("Italy", 3);
                addNationForVP("Turkey", 2);
                addNationForVP("Greece", 1);
                addNationForVP("Far East (E)", 4);
                setSpecialText("Get 10 points once no other nation has more than 12 points.");
                break;
            case "France":
                addNationForVP("Russia", 3);
                addNationForVP("Britain", 2);
                addNationForVP("Italy", 1);
                addNationForVP("Africa (E)", 5);
                setSpecialText("Get 10 points when Germany has not received Treaty Rights from or given Treaty Right to any nation.");
                break;
            case "Germany":
                addNationForVP("Austria", 4);
                addNationForVP("Africa", 3);
                addNationForVP("Russia", 2);
                addNationForVP("Italy", 2);
                setSpecialText("Get 5 points when Britain has not received Treaty Rights from or given Treaty Right to any nation.");
                break;
            case "Russia":
                addNationForVP("Serbia", 5);
                addNationForVP("Romania", 3);
                addNationForVP("Far East", 3);
                addNationForVP("Bulgaria", 1);
                addNationForVP("Greece", 1);
                addNationForVP("Turkey (E)", 5);
                hideSpecialText();
                break;
            case "Austria":
                addNationForVP("Germany", 4);
                addNationForVP("Italy", 2);
                addNationForVP("Romania", 2);
                addNationForVP("Serbia (E)", 10);
                hideSpecialText();
                break;
        }
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
        txtVP.setText(Integer.toString(vp));
        txtVP.setTextSize(18);
        txtVP.setGravity(Gravity.CENTER);
        tr.addView(txtNation);
        tr.addView(txtVP);
        tblVP.addView(tr);
    }

    private void setSpecialText(String text){
        TextView txtSpecial = (TextView) findViewById(R.id.txtSpecial);
        txtSpecial.setText(text);
    }

    private void hideSpecialText(){
        TextView txtSpecial = (TextView) findViewById(R.id.txtSpecial);
        txtSpecial.setVisibility(View.GONE);
    }

    public void onCloseBtn_Click(View view){
        finish();
    }
}
