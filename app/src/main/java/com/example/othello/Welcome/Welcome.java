package com.example.othello.Welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.othello.Activities.MainMenu.MainMenu;
import com.example.othello.R;
import com.example.othello.Service.AudioPlay;
import com.example.othello.Service.MusicBg;

public class Welcome extends AppCompatActivity {

    public static boolean enableMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        //----------------------------
//
//        Intent intent = new Intent(this, MusicBg.class);

        ImageView othello = findViewById(R.id.othello);
        Animation transition = AnimationUtils.loadAnimation(this,R.anim.welcome_transition);
        othello.startAnimation(transition);

        final Intent mainmenu = new Intent(this, MainMenu.class);

        readConfig();

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally{
                    AudioPlay.playAudio(Welcome.this,R.raw.bgmusic);

                    if(!enableMusic){
                        AudioPlay.pauseMusic();
                    }
                    startActivity(mainmenu);
                    finish();
                }
            }
        };

        timer.start();

    }


    private void readConfig(){
        SharedPreferences config = getSharedPreferences("gameConfig", Context.MODE_PRIVATE);

        if(config.contains("Music")){
            String isEnableMusic = config.getString("Music","");

            if(isEnableMusic.equals("true")){
                enableMusic = true;
            }else {
                enableMusic = false;
            }
        }else{
            enableMusic = true;
        }

    }

}
