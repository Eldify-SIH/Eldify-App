package com.sih.eldify.yoga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sih.eldify.R;

public class YogaSecondActivity extends AppCompatActivity {

    String buttonvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_second);

        Intent intent=getIntent();
        buttonvalue=intent.getStringExtra("Value");
        int intvalue=Integer.valueOf(buttonvalue);


        switch (intvalue)
        {
            case 1:
                setContentView(R.layout.activity_boat);
                break;
            case 2:
                setContentView(R.layout.activity_bow);
                break;
            case 3:
                setContentView(R.layout.activity_bridge);
                break;
            case 4:
                setContentView(R.layout.activity_chair);
                break;
            case 5:
                setContentView(R.layout.activity_child);
                break;
            case 6:
                setContentView(R.layout.activity_cobblers);
                break;
            case 7:
                setContentView(R.layout.activity_cow);
                break;
        }


    }



}