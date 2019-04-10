package com.example.othello.Activities.How;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.othello.R;
import com.example.othello.Service.AudioPlay;

import java.util.List;

public class How extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.how);
        //---------------------------------------

        (findViewById(R.id.btnbacktommhow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainMenu(view);
            }
        });

    }

    private void backToMainMenu(View v){
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.MainMenu.MainMenu");
        startActivity(myIntent);
        finish();
    }
}
