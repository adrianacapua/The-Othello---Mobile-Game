package com.example.othello.Activities.Statistics;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.othello.Core.Score.Score;
import com.example.othello.Core.Score.TwoPlayerScore;
import com.example.othello.R;
import com.example.othello.Service.DBHandler;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Statistics extends AppCompatActivity {

    private DBHandler database;

    //TEXTVIEW DATA
    int wins = 0;
    int loses = 0;
    int easy = 0;
    int normal = 0;
    int dif = 0;

    ArrayList<Score> scoreData = null;
    ArrayList<TextView> names = null;
    ArrayList<TextView> score = null;

    ArrayList<TwoPlayerScore> scoreDataTP = null;
    ArrayList<TextView> namesTP = null;
    ArrayList<TextView> scoreTP = null;

    Timer timer=new Timer();//Used for a delay to provide user feedback
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.stats);

        (findViewById(R.id.btnbacktommstats)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainMenu(view);
            }
        });

//        //-----------------------------------------
        database = new DBHandler(this,null,null,1);
        showStatistics();
        handler = new Handler(callback);
    }


    Handler.Callback callback = new Handler.Callback() {
        public boolean handleMessage(Message msg) {

            //WINS LOSES
            ((TextView)(findViewById(R.id.tv_player_wins))).setText(wins+"");
            ((TextView)(findViewById(R.id.tv_player_losses))).setText(loses+"");

            //EASY NORMAL DIFFICULT
            ((TextView)(findViewById(R.id.tv_easy_score))).setText(easy+"");
            ((TextView)(findViewById(R.id.tv_normal_score))).setText(normal+"");
            ((TextView)(findViewById(R.id.tv_difficult_score))).setText(dif+"");

            //TOP NAMES AND SCORES
            for(int i=0; i<scoreData.size(); i++){
//                if(i < scoreData.size() && scoreData != null){
                    names.get(i).setText(scoreData.get(i).getStrPlayerName());
                    score.get(i).setText(scoreData.get(i).getIntScore()+"");
//                }else{
//                    names.get(i).setText("--");
//                    score.get(i).setText("--");
//                }
            }

            //TOP NAMES AND SCORES TWO PLAYER
            for(int i=0; i<5; i++){
                if(i < scoreDataTP.size() && scoreDataTP != null){
                    namesTP.get(i).setText(scoreDataTP.get(i).getStrPlayerName());
                    scoreTP.get(i).setText(scoreDataTP.get(i).getIntScore()+"");
                }else{
                    namesTP.get(i).setText("--");
                    scoreTP.get(i).setText("--");
                }
            }
            return true;
        }
    };

    class SmallDelay extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }


    private void showStatistics(){

        int intWins = database.dbGetTotalOnePlayer(1);
        int intLosses = database.dbGetTotalOnePlayer(0);
        int intEasy = database.dbGetOnePlayerDifWins(1);
        int intNormal = database.dbGetOnePlayerDifWins(2);
        int intDif = database.dbGetOnePlayerDifWins(3);

        //WINS AND LOSES
        wins = intWins;
        loses = intLosses;

        //EASY NORMAL DIFFICULT1
        easy = intEasy;
        normal = intNormal;
        dif = intDif;


        displayTopOnePlayer();
        displayTopTwoPlayer();

        timer.schedule(new SmallDelay(), 100);
    }



    private void displayTopOnePlayer(){
        names = new ArrayList<>();
        score = new ArrayList<>();

        scoreData = database.dbGetTopOnePlayers();

        names.add((TextView)(findViewById(R.id.tv_top_first_name)));
        names.add((TextView)(findViewById(R.id.tv_top_second_name)));
        names.add((TextView)(findViewById(R.id.tv_top_third_name)));
        names.add((TextView)(findViewById(R.id.tv_top_fourth_name)));
        names.add((TextView)(findViewById(R.id.tv_top_fifth_name)));

        score.add((TextView)(findViewById(R.id.tv_top_first_score)));
        score.add((TextView)(findViewById(R.id.tv_top_second_score)));
        score.add((TextView)(findViewById(R.id.tv_top_third_score)));
        score.add((TextView)(findViewById(R.id.tv_top_fourth_score)));
        score.add((TextView)(findViewById(R.id.tv_top_fifth_score)));

    }

    private void displayTopTwoPlayer(){
        namesTP = new ArrayList<>();
        scoreTP = new ArrayList<>();

        scoreDataTP = database.dbGetTopTwoPlayers();

        namesTP.add((TextView)(findViewById(R.id.tv_best_first_name)));
        namesTP.add((TextView)(findViewById(R.id.tv_best_second_name)));
        namesTP.add((TextView)(findViewById(R.id.tv_best_third_name)));
        namesTP.add((TextView)(findViewById(R.id.tv_best_fourth_name)));
        namesTP.add((TextView)(findViewById(R.id.tv_best_fifth_name)));

        scoreTP.add((TextView)(findViewById(R.id.tv_best_first_score)));
        scoreTP.add((TextView)(findViewById(R.id.tv_best_second_score)));
        scoreTP.add((TextView)(findViewById(R.id.tv_best_third_score)));
        scoreTP.add((TextView)(findViewById(R.id.tv_best_fourth_score)));
        scoreTP.add((TextView)(findViewById(R.id.tv_best_fifth_score)));
    }

    private void backToMainMenu(View v){
        Intent myIntent = new Intent();
        myIntent.setClassName("com.example.othello",
                "com.example.othello.Activities.MainMenu.MainMenu");
        startActivity(myIntent);
        finish();
    }
}
