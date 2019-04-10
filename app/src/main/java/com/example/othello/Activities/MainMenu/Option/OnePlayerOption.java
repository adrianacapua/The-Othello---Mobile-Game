package com.example.othello.Activities.MainMenu.Option;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.othello.Player.AI;
import com.example.othello.Player.Human;
import com.example.othello.R;
import com.example.othello.Service.AudioPlay;

import java.util.List;

public class OnePlayerOption extends AppCompatActivity {

    private EditText onePlayerName;
    private RadioButton rbtnEasy;
    private RadioButton rbtnNormal;
    private RadioButton rbtnDifficult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.oneplayer_option);
        //------------------------------------------

        onePlayerName = findViewById(R.id.edtxtopname);

        rbtnEasy = findViewById(R.id.rbtn_easy);
        rbtnNormal = findViewById(R.id.rbtn_normal);
        rbtnDifficult = findViewById(R.id.rbtn_difficulty);

        //ANIMATION--------------------------------
        //playAnim();

    }

    private void playAnim(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView oneplayerHeader = findViewById(R.id.oneplayer_header);
                ImageView brownbg = findViewById(R.id.oneplayeroption_brownbg);
                LinearLayout option = findViewById(R.id.oneplayer_option);
                Button btnGo = findViewById(R.id.btngo);

                Animation fromleft = AnimationUtils.loadAnimation(OnePlayerOption.this,R.anim.fromleft);
                Animation fromright = AnimationUtils.loadAnimation(OnePlayerOption.this,R.anim.fromright);
                oneplayerHeader.setAnimation(fromleft);
                brownbg.setAnimation(fromright);
                option.setAnimation(fromright);
                btnGo.setAnimation(fromright);
            }
        });
    }

    //PLAY-----------------------------------
    public void play(View v) {

        if(onePlayerName.getText().toString().length() <= 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(OnePlayerOption.this,"You forgot to input your name!",Toast.LENGTH_SHORT).show();
                }
            });
        }else if((!rbtnEasy.isChecked()) && (!rbtnNormal.isChecked()) && (!rbtnDifficult.isChecked())){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(OnePlayerOption.this,"Pick a difficulty level!",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            int difficulty=1;

            if(rbtnEasy.isChecked()){
                difficulty = 1;
            }else if(rbtnNormal.isChecked()){
                difficulty = 2;
            }else if(rbtnDifficult.isChecked()){
                difficulty = 3;
            }else{
                Log.e("OnePlayerOption","Input a difficulty!");
            }
            //--------- PLAYER OBJECTS
            AI ai = new AI("Computer",'B',difficulty);
            Human human = new Human(onePlayerName.getText().toString(),'W') ;


            //INTENT--------------------------------
            Intent myIntent = new Intent();
            myIntent.setClassName("com.example.othello",
                    "com.example.othello.Activities.Game.GameOnePlayer");

            myIntent.putExtra("PlayerName",onePlayerName.getText().toString());
            myIntent.putExtra("PlayerOne",ai);
            myIntent.putExtra("PlayerTwo",human);
            myIntent.putExtra("activity","onePlayer");
            myIntent.putExtra("difficulty",difficulty);
            startActivity(myIntent);
            finish();
        }
    }
}