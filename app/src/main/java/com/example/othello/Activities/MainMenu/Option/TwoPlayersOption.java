package com.example.othello.Activities.MainMenu.Option;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.othello.Player.Human;
import com.example.othello.R;

public class TwoPlayersOption extends AppCompatActivity {

    private EditText txtvPlayerOne;
    private EditText txtvPlayerTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.twoplayers_option);
        //------------------------------------------


        txtvPlayerOne = findViewById(R.id.edtxt_opname);
        txtvPlayerTwo = findViewById(R.id.edtxt_tpname);
    }


    //PLAY---------------------------------------
    public void play(View v) {

        if(txtvPlayerOne.getText().toString().length() <= 0 || txtvPlayerTwo.getText().toString().length() <= 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TwoPlayersOption.this,"Both of you must have a name!",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            //--------- PLAYER OBJECTS
            Human playerOne = new Human(txtvPlayerOne.getText().toString(),'B') ;
            Human playerTwo = new Human(txtvPlayerTwo.getText().toString(),'W') ;


            //INTENT----------------------------------
            Intent myIntent = new Intent();
            myIntent.setClassName("com.example.othello",
                    "com.example.othello.Activities.Game.GameTwoPlayer");

            myIntent.putExtra("PlayerOne",playerOne);
            myIntent.putExtra("PlayerTwo",playerTwo);
            myIntent.putExtra("activity","twoPlayer");
            startActivity(myIntent);
            finish();
        }
    }


}