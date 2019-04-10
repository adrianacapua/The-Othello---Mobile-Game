package com.example.othello.Activities.Game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.othello.Core.Board;
import com.example.othello.Core.Score.OnePlayerScore;
import com.example.othello.Core.Score.TwoPlayerScore;
import com.example.othello.Player.AI;
import com.example.othello.Player.Human;
import com.example.othello.Player.Player;
import com.example.othello.R;
import com.example.othello.Service.AudioPlay;
import com.example.othello.Service.DBHandler;
import com.example.othello.Service.SoundPlayer;
import com.example.othello.Welcome.Welcome;

import java.util.ArrayList;

public class GameOnePlayer extends AppCompatActivity implements View.OnClickListener  {

    //MAIN OBJECT
    private Board game;
    private char[][] myBoard;

    //SETTINGS CONFIGURATION
    public static boolean enableSound;
    public static boolean enableHint;

    //DATABASE
    private static DBHandler database;

    //SOUND PLAYER
    private static SoundPlayer soundPlayer;

    //GAME OVER DIALOG---------------------------
    private Dialog gameOverDialog;

    //GAME PAUSED DIALOG
    private Dialog gamePausedDialog;

    //AI difficulty
    private int difficulty = 0;

    //UI
    private static final int COLUMNCOUNT = 8, ROWCOUNT =8;
    private Button[][] btnBoard = new Button[ROWCOUNT][COLUMNCOUNT];
    private int whiteChip = R.drawable.white_field;
    private int blackChip =  R.drawable.black_field;
    private int emptyChip = R.drawable.empty_field;
    private int hintChip = R.drawable.hintfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one_player);

        gameOverDialog  = new Dialog(this);
        gamePausedDialog = new Dialog(this);
        //--------- CREATE BOARD
        createBoard();

        //--------- SET INTENT DATA: NAMES OF THE PLAYERS
        Bundle data = getIntent().getExtras();
        if(data==null){
            return;
        }
        setIntentData(data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //--------- INITIALIZE DATABASE
        database = new DBHandler(this,null,null,1);
        soundPlayer = new SoundPlayer(this);

        if(AudioPlay.isPlayingAudio){
            AudioPlay.pauseMusic();
        }

        //INITIALIZE CONFIG
        readConfig();
    }


    //READ CONFIG SETTINGS
    private void readConfig(){
        SharedPreferences config = getSharedPreferences("gameConfig", Context.MODE_PRIVATE);

        if(config.contains("Sounds") || config.contains("Hint")){
            String isEnableSounds = config.getString("Sounds","");
            String isEnableHint = config.getString("Hint","");

            if(isEnableSounds.equals("true")){
                enableSound = true;
            }else{
                enableSound = false;
            }

            if(isEnableHint.equals("true")){
                enableHint = true;
            }else{
                enableHint = false;
            }
        }else{
            enableSound = true;
            enableHint = true;
        }
    }

    //SET INTENT DATA
    private void setIntentData(Bundle data){
        TextView tvPlayerOneName = findViewById(R.id.txtvopname);
        TextView tvPlayerTwoName = findViewById(R.id.txtvtpname);

        Player playerOne = (Player) getIntent().getSerializableExtra("PlayerOne");
        Player playerTwo = (Player) getIntent().getSerializableExtra("PlayerTwo");

        tvPlayerOneName.setText(playerOne.getStrName());
        tvPlayerTwoName.setText(playerTwo.getStrName());

        difficulty = (Integer) getIntent().getSerializableExtra("difficulty");

        newGame(playerOne,playerTwo);
    }

    //NEW GAME
    private void newGame(Player playerOne, Player playerTwo){
        game = new Board(playerOne,playerTwo);
        game.initializeBoard();
        game.setCurrentPlayer(playerTwo);
    }

    //ACTIVITY
    private void restartActivity(){

        this.recreate();
    }

    //WRITE TO DATABASE
    private void writeToDB(final Player playerOne, final Player playerTwo, final int scoreOne, final int scoreTwo){

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    int intWin = (scoreTwo>scoreOne)? 1 : 0;

                    database.addOnePlayerScore(new OnePlayerScore(playerTwo.getStrName(),
                            scoreTwo,((AI) playerOne).getDif(),intWin));

                } catch (Exception ex) {

                    Log.e("writeToDB", ex.toString());
                }

            }
        }).start();
    }

    public void createBoard(){

        TableLayout board = findViewById(R.id.othello_board);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1f);
        layoutParams.setMargins(1,1,1,1);


        TableRow tblRow;
        int id = 0;

        for(int row = 0; row < ROWCOUNT; row++){

            tblRow = new TableRow(this);
            tblRow.setGravity(Gravity.CENTER_HORIZONTAL);

            for(int col = 0; col < COLUMNCOUNT; col++){

                btnBoard[row][col] = new Button(this);
                btnBoard[row][col].setId(id);
                btnBoard[row][col].setTag(R.id.xpos,row);
                btnBoard[row][col].setTag(R.id.ypos,col);
                btnBoard[row][col].setBackgroundResource(emptyChip);
                btnBoard[row][col].setLayoutParams(layoutParams);

                btnBoard[row][col].setOnClickListener(this);

                tblRow.addView(btnBoard[row][col]);
                id++;

            }

            board.addView(tblRow,new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,1f));

        }

        btnBoard[3][3].setBackgroundResource(R.drawable.black_field);
        btnBoard[3][4].setBackgroundResource(R.drawable.white_field);
        btnBoard[4][3].setBackgroundResource(R.drawable.white_field);
        btnBoard[4][4].setBackgroundResource(R.drawable.black_field);

    }

    //GAME OVER
    private void gameOver(){

        Toast.makeText(getApplicationContext(),"GAME OVER!!!",Toast.LENGTH_LONG).show();

        Player playerOne = game.getPlayerOne();
        Player playerTwo = game.getPlayerTwo();

        int scoreOne = playerOne.getScore(game); //BLACK
        int scoreTwo = playerTwo.getScore(game); //WHITE

        //WRITE SCORES TO DATABASE
        writeToDB(playerOne,playerTwo,scoreOne,scoreTwo);

        //POPUP--------------------------------
        showGameOverDialog(scoreOne,playerOne.getStrName(),scoreTwo,playerTwo.getStrName());

    }

    //RELOAD SCORE
    private void reloadScore(){
        int whiteScore = game.getScore('W');
        int blackScore = game.getScore('B');

        TextView txtvWhite = findViewById(R.id.score_player_two);
        TextView txtvBlack = findViewById(R.id.score_player_one);

        txtvWhite.setText(whiteScore+"");
        txtvBlack.setText(blackScore+"");
    }

    //SHOW GAME OVER DIALOG
    private void showGameOverDialog(final int blackScore, final String blackName,
                                    final int whiteScore, final String whiteName){

        gameOverDialog.setContentView(R.layout.gameover);

        final TextView tvBlackScore = gameOverDialog.findViewById(R.id.go_black_score);
        final TextView tvWhiteScore = gameOverDialog.findViewById(R.id.go_white_score);

        final TextView tvBlackName = gameOverDialog.findViewById(R.id.black_player_name);
        final TextView tvWhiteName = gameOverDialog.findViewById(R.id.white_player_name);

        ImageView playAgain = gameOverDialog.findViewById(R.id.play_again);
        ImageView backMainMenu = gameOverDialog.findViewById(R.id.main_menu);


        //!!!!!!!!!!!!!!
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOverDialog.dismiss();
                restartActivity();
            }
        });

        //!!!!!!!!!!!!!!
        backMainMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                gameOverDialog.dismiss();

                if(Welcome.enableMusic){
                    AudioPlay.resumeMusic();
                }

                Intent myIntent = new Intent();
                myIntent.setClassName("com.example.othello",
                        "com.example.othello.Activities.MainMenu.MainMenu");
                startActivity(myIntent);
                finish();
            }
        });



        runOnUiThread(new Runnable() {

            public void run() {
                //SET DATA
                tvBlackName.setText(blackName);
                tvWhiteName.setText(whiteName);
                tvBlackScore.setText(blackScore+"");
                tvWhiteScore.setText(whiteScore+"");

                //

                gameOverDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                gameOverDialog.show();
            }
        });

    }

    public void onBackPressed() {

        gamePausedDialog.setContentView(R.layout.pausegame);

        ImageView btnResume = gamePausedDialog.findViewById(R.id.resume_game);
        ImageView btnRestart = gamePausedDialog.findViewById(R.id.restart_game);
        ImageView btnMainMenu = gamePausedDialog.findViewById(R.id.mainmenu_game);

        btnResume.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                gamePausedDialog.dismiss();
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(GameOnePlayer.this);
                builder.setCancelable(false);
                builder.setMessage("Are you sure you want to restart the game?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        restartActivity();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                gamePausedDialog.dismiss();
            }
        });

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(GameOnePlayer.this);
                builder.setCancelable(false);
                builder.setMessage("Are you sure you want to end the game?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent myIntent = new Intent();
                        myIntent.setClassName("com.example.othello",
                                "com.example.othello.Activities.MainMenu.MainMenu");
                        startActivity(myIntent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                gamePausedDialog.dismiss();
            }
        });

        runOnUiThread(new Runnable() {

            public void run() {
                gamePausedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                gamePausedDialog.show();
            }
        });
    }

    private void updateGUI(){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                try {

                    ArrayList<Point> points = game.getMoveListXY();
                    //int pCtr=0;

                    for(int x=0; x<=7; x++){
                        for(int y=0; y<=7; y++){

                            if(myBoard[x][y]=='W'){
                                btnBoard[x][y].setBackgroundResource(whiteChip);
                            }else if(myBoard[x][y]=='B'){
                                btnBoard[x][y].setBackgroundResource(blackChip);
                            }else{
                                btnBoard[x][y].setBackgroundResource(emptyChip);
                            }

                        }
                    }

                    if(enableHint){
                        if (Human.class.isInstance(game.getCurrentPlayer())) {
                            for(int ctr=0; ctr<points.size(); ctr++){
                                btnBoard[points.get(ctr).x][points.get(ctr).y].setBackgroundResource(hintChip);
                            }
                        }
                    }
                    reloadScore();



                } catch (Exception ex) {

                    Log.e("updateGUI", ex.toString());
                }

            }
        });


    }

    //SET BUTTON ENABLE
    private void setButton(final boolean flag){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    Resources res = getResources();
                    if(flag){
                        for (int i = 0; i < 64; i++) {
                            String b = "" + i;
                            Button button = findViewById(res.getIdentifier(b, "id", getPackageName()));
                            button.setEnabled(true);
                        }
                    }else{
                        for (int i = 0; i < 64; i++) {
                            String b = "" + i;
                            Button button = findViewById(res.getIdentifier(b, "id", getPackageName()));
                            button.setEnabled(false);
                        }
                    }

                } catch (Exception ex) {

                    Log.e("setButton", ex.toString());
                }

            }
        });


    }

    //ONCLICK------------------------------
    @Override
    public void onClick(View view) {

        if(!game.gameOver()){

            //HUMAN-------------------------
            if(Human.class.isInstance(game.getCurrentPlayer())){
                callHuman(view);
            }

            //AI----------------------------
            if(AI.class.isInstance(game.getCurrentPlayer())){

                callAI();
            }

            if(difficulty==1){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e("callHuman",e.getMessage());
                }
            }

        }else{
            gameOver();
            return;
        }
    }

    //CALL HUMAN---------------------------
    private void callHuman(View view){

        Button btn = findViewById(view.getId());

        int xPos = (Integer) btn.getTag(R.id.xpos);
        int yPos = (Integer) btn.getTag(R.id.ypos);

        if(game.isValidMove(xPos,yPos)){

            if(enableSound){
                soundPlayer.playFlipSound();
            }

            game.makeMove(xPos,yPos);
            game.changePlayer();

            //myBoard = game.getBoard();
            myBoard = game.getBoard();
            updateGUI();


        }else{
            //INVALID MOVE!
            Toast.makeText(getApplicationContext(),"INVALID MOVE!",Toast.LENGTH_LONG).show();

        }


        //CHECKS IF THERE'S A MOVE FOR NEXT PLAYER-------------------------
        if(game.getMoveList(new int[60], new int[60]) == 0){
            Log.e("NO MOVES","CHANGE PLAYER");
            game.changePlayer();
            updateGUI();
        }

        if(game.gameOver()){
            gameOver();
            return;
        }



    }

    //CALL AI------------------------------
    private void callAI(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    while(AI.class.isInstance(game.getCurrentPlayer())){

                        setButton(false);


                        AI ai = (AI) game.getCurrentPlayer();
                        ai.move(game);

                        int xPos = ai.getxPos();
                        int yPos = ai.getyPos();

                        game.makeMove(xPos,yPos);
                        myBoard = game.getBoard();

                        if(enableSound){
                            soundPlayer.playFlipSound();
                        }

                        updateGUI();
                        game.changePlayer();
                        Log.e("PLAYER","INSIDE");
                        Log.e("PLAYER",game.getCurrentPlayer().getStrName());

                        //CHECKS IF THERE'S A MOVE FOR NEXT PLAYER-------------------------
                        if(game.getMoveList(new int[60], new int[60]) == 0){
                            Log.e("NO MOVES","CHANGE PLAYER");
                            game.changePlayer();
                            updateGUI();
                        }

                        setButton(true);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if(game.gameOver()){
                                    gameOver();
                                    return;
                                }
                            }
                        });



                    }


                } catch (Exception ex) {

                    Log.e("callAI", ex.toString());
                }

            }
        }).start();


    }

}
