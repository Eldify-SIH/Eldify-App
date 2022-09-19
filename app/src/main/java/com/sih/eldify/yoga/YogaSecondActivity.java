package com.sih.eldify.yoga;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sih.eldify.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class YogaSecondActivity extends AppCompatActivity {

    String buttonvalue;
    Button button;
    
    TextToSpeech textToSpeech;

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

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // check if its success
                if (i == TextToSpeech.SUCCESS) {

                    // set language
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    // check if there is any missing data or the lang is supported or not
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                        // if true
                        Log.e("logtts", "Language not supported");
                    }
                    else{
                        // if false
                        Log.e("logtts", "Language supported");
                    }
                }
                else{
                    // if success is false
                    Log.e("logtts", "Initialization failed");
                }
            }
        });

        button=findViewById(R.id.startbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (intvalue)
                {
                    case 1:
                        speak(getString(R.string.boat_pose));
                        break;
                    case 2:
                        speak(getString(R.string.bow_pose));
                        break;
                    case 3:
                        speak(getString(R.string.bridge_pose));
                        break;
                    case 4:
                        speak(getString(R.string.chair_pose));
                        break;
                    case 5:
                        speak(getString(R.string.child_pose));
                        break;
                    case 6:
                        speak(getString(R.string.cobblers_pose));
                        break;
                    case 7:
                        speak(getString(R.string.cow_pose));
                        break;
                }
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        textToSpeech.stop();
        textToSpeech.shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.stop();
        textToSpeech.shutdown();
    }

    private void speak(String s) {
        textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null, "");
    }


}