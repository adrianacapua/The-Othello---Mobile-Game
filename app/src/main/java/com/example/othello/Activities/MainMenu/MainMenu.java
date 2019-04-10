package com.example.othello.Activities.MainMenu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.othello.R;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_menu);
        //---------------------------------------

        //ANIMATION--------------------------
        //playAnim();
    }

    public void playAnim(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation fromBottomAnim = AnimationUtils.loadAnimation(MainMenu.this,R.anim.frombottom);
                Animation fromTopAnim = AnimationUtils.loadAnimation(MainMenu.this,R.anim.fromtop);
                ImageView header = findViewById(R.id.header);
                LinearLayout mainMenu = findViewById(R.id.mainmenu);
                header.setAnimation(fromTopAnim);
                mainMenu.setAnimation(fromBottomAnim);
            }
        });
    }

    //-----------------------------MAIN MENU--------------------------------

    //PLAY--------------------------------
    public void play(View v) {
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.Game.GameOption");
        startActivity(myIntent);
    }

    //STATS--------------------------------
    public void stats(View v) {
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.Statistics.Statistics");
        startActivity(myIntent);
    }

    //HOW--------------------------------
    public void how(View v) {
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.How.How");
        startActivity(myIntent);
    }

    //SETTINGS--------------------------------
    public void settings(View v) {
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.Settings.Settings");
        startActivity(myIntent);
    }

    //ABOUT--------------------------------
    public void about(View v) {
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.About.About");
        startActivity(myIntent);
    }
}
