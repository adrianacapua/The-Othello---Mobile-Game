package com.example.othello.Activities.About;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.othello.R;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.about);
    }

    public void backToMainMenu(View v){
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.MainMenu.MainMenu");
        startActivity(myIntent);
        finish();
    }

}
