package com.example.othello.Player;

import android.graphics.Point;
import android.support.annotation.Nullable;

import com.example.othello.Core.Board;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class AI extends Player implements Serializable{

    private int xPos;
    private int yPos;
    private int difficulty;


    public AI(){

    }

    public AI(String name, char field, int difficulty){
        super("Computer",field);
        this.difficulty = difficulty;
    }

    //GETTERS AND SETTER
    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getDif(){ return difficulty; }



    //METHODS

    public void move(Board game){

        switch(difficulty){
            case 1: getRandomMove(game);break;
            case 2: minimaxDecision(game);break;
            case 3: strategyMove(game);break;
        }

    }

    //EASY
    public void getRandomMove(Board game){

        int[] moveX = new int[60];
        int[] moveY = new int[60];

        int numMoves = 0;

        numMoves = game.getMoveList(moveX,moveY);

        if(numMoves==0){
            yPos=-1;
            xPos=-1;
        }else{

//            Random r = new Random();
//            int i = r.nextInt(((numMoves-1) - 0) + 1);

            int i = (int)Math.random() % numMoves;
            xPos = moveX[i];
            yPos = moveY[i];
        }
    }

    //NORMAL
    public void minimaxDecision(Board game){

        int[] moveX = new int[60];
        int[] moveY = new int[60];
        int numMoves = 0;

        numMoves = game.getMoveList(moveX,moveY);

        if(numMoves == 0){
            xPos = -1;
            yPos = -1;
        }else{

            //START AS MAXIMIXING PLAYER
            int bestMoveValue = -Integer.MAX_VALUE;
            int bestX = moveX[0];
            int bestY = moveY[0];

            for(int i=0; i<numMoves; i++){
                int val=0;

                Board temp = (Board) deepClone(game);

                temp.makeMove(moveX[i],moveY[i]);
                temp.changePlayer();

                val = alphaBeta(temp,3,-Integer.MAX_VALUE,Integer.MAX_VALUE,false,game.getCurrentField());

                if(val > bestMoveValue){
                    bestMoveValue = val;
                    bestX = moveX[i];
                    bestY = moveY[i];
                }
            }

            xPos = bestX;
            yPos = bestY;
        }
    }

    private int alphaBeta(Board game, int depth, int alpha, int beta, boolean maximingPlayer, char origTurn){

        if(depth==0 || game.gameOver()){
            return game.getHeuristic(origTurn);
        }

        int[] moveX = new int[60];
        int[] moveY = new int[60];

        int numMoves = 0;

//        char opponent = game.getOpponentField();

        numMoves = game.getMoveList(moveX,moveY);


        if(maximingPlayer){
            for(int i=0; i<numMoves; i++){

                Board child = (Board) deepClone(game);
                if (child != null) {
                    child.makeMove(moveX[i], moveY[i]);
                    child.changePlayer();
                }


                alpha = Math.max(alpha, alphaBeta(child,depth-1,alpha,beta,false,origTurn));

                if(alpha>=beta)
                    break;
            }

            return alpha;

        }else if(!maximingPlayer){
            for(int i=0; i<numMoves; i++){

                Board child = (Board) deepClone(game);
                if (child != null) {
                    child.makeMove(moveX[i], moveY[i]);
                    child.changePlayer();
                }


                beta = Math.min(beta, alphaBeta(child,depth-1,alpha,beta,true,origTurn));

                if(alpha>=beta)
                    break;
            }

            return beta;
        }
        return - 1;
    }

    //DIFFICULT
    public void strategyMove(Board game){


        int[] moveX = new int[60];
        int[] moveY = new int[60];

        int numMoves = 0;

        numMoves = game.getMoveList(moveX,moveY);

        if(numMoves==0){
            yPos=-1;
            xPos=-1;
        }else{

            ArrayList<Point> bestMoves = game.getMoveListXY();
            Collections.shuffle(bestMoves);

            for(int i=0; i<bestMoves.size(); i++){
                if((bestMoves.get(i).x == 0 || bestMoves.get(i).x == 7) &&
                        (bestMoves.get(i).y == 0 || bestMoves.get(i).y == 7)){
                    xPos = bestMoves.get(i).x;
                    yPos = bestMoves.get(i).y;
                    return;
                }
            }
        }
        
        //IF NO BEST MOVE AVAILABLE PICK MINIMAXDECISION
        minimaxDecision(game);

    }



    //--------------------------------------------
    @Nullable
    private static Object deepClone(Object object){

        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }catch(Exception e){

            e.printStackTrace();
            return null;
        }
    }


}

