package com.example.othello.Core.Score;

/**
 * Created by nim on 27/01/2018.
 */

public class Score {

    private String strPlayerName;
    private int intScore;

    public Score(String strPlayerName, int intScore){
        this.strPlayerName = strPlayerName;
        this.intScore = intScore;
    }

    public String getStrPlayerName() {
        return strPlayerName;
    }

    public void setStrPlayerName(String strPlayerName) {
        this.strPlayerName = strPlayerName;
    }

    public int getIntScore() {
        return intScore;
    }

    public void setIntScore(int intScore) {
        this.intScore = intScore;
    }


}
