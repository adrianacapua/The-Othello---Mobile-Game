package com.example.othello.Activities.Game;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.othello.R;
import com.example.othello.Service.AudioPlay;

import java.util.List;

public class GameOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_option);
        //--------------------------------------------

    }

    //ONE PLAYER-------------------------------
    public void setOnePlayer(View v) {
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.MainMenu.Option.OnePlayerOption");
        startActivity(myIntent);
        finish();
    }

    //TWO PLAYER-------------------------------
    public void setTwoPlayers(View v) {
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.MainMenu.Option.TwoPlayersOption");
        startActivity(myIntent);
        finish();
    }
}
