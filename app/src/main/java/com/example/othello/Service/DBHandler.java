package com.example.othello.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.othello.Core.Score.OnePlayerScore;
import com.example.othello.Core.Score.Score;
import com.example.othello.Core.Score.TwoPlayerScore;

import java.util.ArrayList;

/**
 * Created by nim on 27/01/2018.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Score.db";

    //first PLAYER TABLE
    private static final String TABLE_ONEPLAYER = "OnePlayer";
    private static final String OP_ID = "ID";
    private static final String OP_PLAYERNAME = "PlayerName";
    private static final String OP_SCORE = "Score";
    private static final String OP_DLEVEL = "DifLevel";
    private static final String OP_ISWIN = "IsWin";

    //second PLAYER TABLE
    private static final String TABLE_TWOPLAYER = "TwoPlayer";
    private static final String TP_ID = "ID";
    private static final String TP_PLAYERNAME = "PlayerName";
    private static final String TP_SCORE = "Score";

    //CONSTRUCTOR
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryOPT = createOnePlayerTable();
        String queryTPT = createTwoPlayerTable();

        try{
            db.execSQL(queryOPT);
            db.execSQL(queryTPT);
            Log.e("my app", "hello");
        }catch(SQLiteException e){
            Log.e("my app", e.toString());
        }

    }

    private String createOnePlayerTable(){
        String query = "CREATE TABLE " + TABLE_ONEPLAYER + " (" +
                OP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OP_PLAYERNAME + " TEXT, " +
                OP_SCORE + " INTEGER, " +
                OP_DLEVEL + " INTEGER, " +
                OP_ISWIN + " INTEGER " +
                ");";

        return query;
    }

    private String createTwoPlayerTable(){
        String query = "CREATE TABLE " + TABLE_TWOPLAYER + "( " +
                TP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TP_PLAYERNAME + " TEXT, " +
                TP_SCORE + " INTEGER );";

        return query;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONEPLAYER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWOPLAYER);
        onCreate(db);
    }

    //ADD NEW ONEPLAYERSCORE
    public void addOnePlayerScore(OnePlayerScore score){
        ContentValues values = new ContentValues();
        values.put(OP_PLAYERNAME,score.getStrPlayerName());
        values.put(OP_SCORE,score.getIntScore());
        values.put(OP_DLEVEL,score.getIntDifLevel());
        values.put(OP_ISWIN,score.getIntWin());

        SQLiteDatabase db = getWritableDatabase();

        try{
            db.insert(TABLE_ONEPLAYER,null,values);
        }
        catch(SQLiteException e){
            Log.e("my app", e.toString());
        }
        db.close();
    }

    //ADD NEW TWOPLAYERSCORE
    public void addTwoPlayerScore(TwoPlayerScore score){
        ContentValues values = new ContentValues();
        values.put(TP_PLAYERNAME,score.getStrPlayerName());
        values.put(TP_SCORE,score.getIntScore());

        SQLiteDatabase db = getWritableDatabase();

        try{
            db.insert(TABLE_TWOPLAYER,null,values);
        }
        catch(SQLiteException e){
            Log.e("my app", e.toString());
        }
        db.close();
    }

    //GET TOP 5 ONEPLAYER
    public ArrayList<Score> dbGetTopOnePlayers(){
        ArrayList<Score>  topOnePlayerList = new ArrayList<>();
        String tempName;
        int tempScore;

        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT " + OP_PLAYERNAME + "," + OP_SCORE +
                " FROM " + TABLE_ONEPLAYER +
                " ORDER BY " + OP_SCORE + " DESC" +
                " LIMIT 5;";

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);

        //move to the first row
        c.moveToFirst();

        try{
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex(OP_PLAYERNAME))!=null){
                    tempName = c.getString(c.getColumnIndex(OP_PLAYERNAME));
                    tempScore = Integer.parseInt(c.getString(c.getColumnIndex(OP_SCORE)));
                    topOnePlayerList.add(new Score(tempName,tempScore));
                    c.moveToNext();
                    Log.e("TOP1PLAYER",tempName + " " + tempScore);
                }
            }
        }catch(SQLiteException e){
            Log.e("my app", e.toString());
        }


        db.close();
        return topOnePlayerList;
    }

    //GET TOP 5 TWOPLAYER
    public ArrayList<TwoPlayerScore> dbGetTopTwoPlayers(){
        ArrayList<TwoPlayerScore>  topTwoPlayerList = new ArrayList<>();
        String tempName;
        int tempScore;

        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT " + TP_PLAYERNAME + "," + TP_SCORE +
                " FROM " + TABLE_TWOPLAYER +
                " ORDER BY " + TP_SCORE + " DESC" +
                " LIMIT 5;";

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);

        //move to the first row
        c.moveToFirst();

        try{
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex(TP_PLAYERNAME))!=null){
                    tempName = c.getString(c.getColumnIndex(TP_PLAYERNAME));
                    tempScore = Integer.parseInt(c.getString(c.getColumnIndex(TP_SCORE)));
                    topTwoPlayerList.add(new TwoPlayerScore(tempName,tempScore));
                    c.moveToNext();
                }
            }
        }catch(SQLiteException e){
            Log.e("my app", e.toString());
        }


        db.close();
        return topTwoPlayerList;
    }

    //GET ONE PLAYER WIN PER DIF LEVEL
    public int dbGetOnePlayerDifWins(int intDif){

        int intNumberOfWins=0;
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT COUNT(" + OP_ISWIN + ")" +
                " FROM " + TABLE_ONEPLAYER +
                " WHERE " + OP_DLEVEL + " = " + intDif +
                            " AND " + OP_ISWIN + " = 0;";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        try{
            intNumberOfWins = c.getInt(0);
        }catch(SQLiteException e){
            Log.e("dbGetOnePlayerDifWins", e.toString());
        }

        return intNumberOfWins;
    }

    //GET OVERALL ONE PLAYER WINS OR LOSES
    public int dbGetTotalOnePlayer(int intWin){

        int intTotal=0;
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT COUNT(" + OP_ISWIN + ")" +
                " FROM " + TABLE_ONEPLAYER +
                " WHERE " + OP_ISWIN + " = " + String.valueOf(intWin) + ";";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        try{
            intTotal = c.getInt(0);
        }catch(SQLiteException e){
            Log.e("dbGetTotalOnePlayer", e.toString());
        }
        return intTotal;
    }


}
