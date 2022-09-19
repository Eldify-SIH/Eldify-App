package com.sih.eldify.yoga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sih.eldify.R;

public class YogaFirstActicity extends AppCompatActivity {


    int[] newArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_first_acticity);

        newArray=new int[]{
                R.id.boatpose,R.id.bowpose,R.id.bridgepose,R.id.chairpose,
                R.id.childpose,R.id.cobblerspose,R.id.cowpose
        };
    }

    public void ImageButtononClicked(View view)
    {
        for (int i=0;i<newArray.length;i++)
        {
            if(view.getId()==newArray[i])
            {
                int value=i+1;
                Intent intent=new Intent(YogaFirstActicity.this, YogaSecondActivity.class);
                intent.putExtra("Value",String.valueOf(value));
                startActivity(intent);
            }

        }
    }
}