package com.example.originsofworldwari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity {

    private String header;
    private int currentPage, totalPage;
    private int[] imgRules = {
            R.drawable.rule1,
            R.drawable.rule2,
            R.drawable.rule3,
            R.drawable.rule4,
            R.drawable.rule5,
            R.drawable.rule6,
            R.drawable.rule7,
            R.drawable.rule8,
            R.drawable.rule9
    };
    private int[] txtRules = {
            R.string.rule1,
            R.string.rule2,
            R.string.rule3,
            R.string.rule4,
            R.string.rule5,
            R.string.rule6,
            R.string.rule7,
            R.string.rule8,
            R.string.rule9
    };

    private int[] imgPlaying = {
            R.drawable.playing1,
            R.drawable.playing2,
            R.drawable.playing3,
            R.drawable.playing4,
            R.drawable.playing5,
            R.drawable.playing6,
            R.drawable.playing7,
            R.drawable.playing8,
            R.drawable.playing9,
            R.drawable.playing10,
            R.drawable.playing11,
            R.drawable.playing12,
            R.drawable.playing13,
            R.drawable.playing14,
            R.drawable.playing15,
            R.drawable.playing16,
            R.drawable.playing17
    };
    private int[] txtPlaying = {
            R.string.playing1,
            R.string.playing2,
            R.string.playing3,
            R.string.playing4,
            R.string.playing5,
            R.string.playing6,
            R.string.playing7,
            R.string.playing8,
            R.string.playing9,
            R.string.playing10,
            R.string.playing11,
            R.string.playing12,
            R.string.playing13,
            R.string.playing14,
            R.string.playing15,
            R.string.playing16,
            R.string.playing17
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        setHeader();
        setTotalPage();

        currentPage = 1;
        setInfo(currentPage);
    }

    private void setHeader(){
        TextView lblHeader = (TextView) findViewById(R.id.lblHeader);
        Intent intent = getIntent();
        this.header = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        lblHeader.setText(this.header);
    }

    @SuppressLint("SetTextI18n")
    private void setTotalPage(){
        if (this.header.equals("Rules"))
            totalPage = txtRules.length;
        else
            totalPage = txtPlaying.length;

        TextView txtTotalPages = (TextView) findViewById(R.id.txtTotalPages);
        txtTotalPages.setText(Integer.toString(totalPage));
    }

    @SuppressLint("SetTextI18n")
    private void setInfo(int num){
        TextView txtPage = (TextView) findViewById(R.id.txtPage);
        txtPage.setText(Integer.toString(num));

        ImageView imgDetail = (ImageView) findViewById(R.id.imgDetail);
        TextView txtDetail = (TextView) findViewById(R.id.txtDetail);
        if (this.header.equals("Rules")){
            imgDetail.setImageDrawable(getResources().getDrawable(imgRules[num-1]));
            txtDetail.setText(getResources().getString(txtRules[num-1]));
        }
        else{
            imgDetail.setImageDrawable(getResources().getDrawable(imgPlaying[num-1]));
            txtDetail.setText(getResources().getString(txtPlaying[num-1]));
        }

        Button btnPrevious = (Button) findViewById(R.id.btnPrevious);
        if (num == 1)
            disableButton(btnPrevious);
        else
            enableButton(btnPrevious);

        Button btnNextPage = (Button) findViewById(R.id.btnNext);
        if (num == totalPage)
            disableButton(btnNextPage);
        else
            enableButton(btnNextPage);
    }

    private void disableButton(Button btn){
        btn.setClickable(false);
        btn.setEnabled(false);
    }

    private void enableButton(Button btn){
        btn.setClickable(true);
        btn.setEnabled(true);
    }

    public void onPreviousBtn_Click(View view){
        currentPage -= 1;
        setInfo(currentPage);
    }

    public void onNextBtn_Click(View view){
        currentPage += 1;
        setInfo(currentPage);
    }

    public void onBackBtn_Click(View view){
        Intent intentMain = new Intent(InformationActivity.this, MainActivity.class);
        InformationActivity.this.startActivity(intentMain);
    }
}
