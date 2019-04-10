package com.example.othello.Service;

/**
 * Created by nim on 24/02/2018.
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class AudioPlay {

    public static MediaPlayer mediaPlayer;
    private static SoundPool soundPool;
    public static boolean isPlayingAudio = false;
    public static int length = 0;

    public static void playAudio(Context c, int id){

        mediaPlayer = MediaPlayer.create(c,id);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
        soundPool = new SoundPool(4,AudioManager.STREAM_MUSIC,100);

        if(!mediaPlayer.isPlaying()){
            isPlayingAudio = true;
            mediaPlayer.start();
        }
    }

    public static void stopAudio(){
        isPlayingAudio=false;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public static void pauseMusic(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            length  = mediaPlayer.getCurrentPosition();
        }
    }

    public static void resumeMusic(){
        if(mediaPlayer.isPlaying()==false){
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
        }
    }

}
