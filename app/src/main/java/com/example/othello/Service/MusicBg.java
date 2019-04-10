package com.example.othello.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.example.othello.R;

/**
 * Created by nim on 20/01/2018.
 */

public class MusicBg extends Service {

    private MediaPlayer player;
    private int length;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
        player = MediaPlayer.create(this,R.raw.bgmusic);
        player.setLooping(true);
        player.setVolume(100,100);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //starting the player
        player.start();

        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }

    public void pauseMusic(){
        if(player.isPlaying()){
            player.pause();
            length  = player.getCurrentPosition();
        }
    }

    public void resumeMusic(){
        if(player.isPlaying()==false){
            player.seekTo(length);
            player.start();
        }
    }

    public void stopMusic(){
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        super.onDestroy();
        if(player != null)
        {
            try{
                player.stop();
                player.release();
            }finally {
                player = null;
            }
        }
    }


}
