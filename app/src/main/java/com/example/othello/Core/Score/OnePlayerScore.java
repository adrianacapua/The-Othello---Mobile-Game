package com.example.othello.Core.Score;

/**
 * Created by nim on 27/01/2018.
 */

public class OnePlayerScore extends Score{

    private int intDifLevel;
    private int intWin =0; //first- won 0-lose

    public OnePlayerScore(String strPlayerName, int intScore, int intDifLevel, int intWin){
        super(strPlayerName,intScore);
        this.intDifLevel = intDifLevel;
        this.intWin = intWin;
    }

    public int getIntDifLevel() {
        return intDifLevel;
    }

    public void setIntDifLevel(int intDifLevel) {
        this.intDifLevel = intDifLevel;
    }

    public int getIntWin() {
        return intWin;
    }

    public void setIntWin(int won) {
        intWin = won;
    }
}
