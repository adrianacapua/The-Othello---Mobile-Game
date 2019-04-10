package com.example.othello.Player;


import com.example.othello.Core.Board;

import java.io.Serializable;


public class Player implements Serializable {

    private String strName;
    private char chField;

    private int intScore;

    public Player(){

    }

    public Player(String name, char field){

        strName = name;
        chField = field;
        intScore = 0;

    }

    public String getStrName(){
        return strName;
    }

    public char getChField() {
        return chField;
    }

    //METHODS
    public int getScore(Board board){ //returns the number of pieces of the player in the board

        int total = 0;
        for(int intCtr=0; intCtr<8; intCtr++){

            for(int subCtr=0; subCtr<8; subCtr++){

                if(board.getBoard()[intCtr][subCtr] == this.getChField())
                    total++;
            }
        }

        return total;
    }
}


