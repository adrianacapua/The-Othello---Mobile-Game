package com.example.othello.Activities.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;

import com.example.othello.Activities.Game.GameOnePlayer;
import com.example.othello.Activities.Game.GameTwoPlayer;
import com.example.othello.R;
import com.example.othello.Service.AudioPlay;
import com.example.othello.Welcome.Welcome;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    private Switch switchMusic = null;
    private Switch switchSounds = null;
    private Switch switchHint = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.settings);
        //------------------------------------------

        switchMusic = findViewById(R.id.switchMusic);
        switchSounds = findViewById(R.id.switchSounds);
        switchHint = findViewById(R.id.switchHint);

    }

    @Override
    protected void onStart() {

        readConfig();
        super.onStart();
    }

    private void readConfig(){
        SharedPreferences config = getSharedPreferences("gameConfig",Context.MODE_PRIVATE);
        if(config.contains("Music")){
            if(config.getString("Music","").equals("true")){
                switchMusic.setChecked(true);
            }else{
                switchMusic.setChecked(false);
            }
        }

        if(config.contains("Sounds")){
            if(config.getString("Sounds","").equals("true")){
                switchSounds.setChecked(true);
            }else{
                switchSounds.setChecked(false);
            }
        }

        if(config.contains("Hint")){
            if(config.getString("Hint","").equals("true")){
                switchHint.setChecked(true);
            }else{
                switchHint.setChecked(false);
            }
        }

    }

    @Override
    public void onClick(View view) {
        SharedPreferences config = getSharedPreferences("gameConfig",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = config.edit();

        if(view == switchMusic){

            boolean status = ((Switch) view).isChecked();

            if(status){
                AudioPlay.resumeMusic();
            }else{
                AudioPlay.pauseMusic();
            }

            Welcome.enableMusic = status;
            editor.putString("Music",String.valueOf(status));

        }else if(view == switchSounds){

            boolean status = ((Switch) view).isChecked();
            GameOnePlayer.enableSound = status;
            GameTwoPlayer.enableSound = status;
            editor.putString("Sounds",String.valueOf(status));

        }else if(view == switchHint){

            boolean status = ((Switch) view).isChecked();
            GameOnePlayer.enableHint = status;
            GameTwoPlayer.enableHint = status;
            editor.putString("Hint",String.valueOf(status));
        }

        editor.apply();
    }



    public void goToHow(View v) {
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.How.How");
        startActivity(myIntent);
    }

    public void backToMainMenu(View v) {
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.MainMenu.MainMenu");
        startActivity(myIntent);
        finish();
    }


}
