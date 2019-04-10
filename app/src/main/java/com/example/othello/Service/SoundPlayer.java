package com.example.othello.Service;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.othello.R;

/**
 * Created by nim on 24/02/2018.
 */

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int flipSound;

    public SoundPlayer(Context context){

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flipSound = soundPool.load(context, R.raw.flip,1);
    }

    public void playFlipSound(){
        soundPool.play(flipSound, 1.0f,1.0f,1,0,1.0f);
    }
}
