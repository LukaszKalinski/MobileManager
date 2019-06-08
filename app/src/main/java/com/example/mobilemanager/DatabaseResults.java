package com.example.mobilemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseResults {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_DATE = "DATE";
    public static final String KEY_HOME_TEAM = "HomeTeam";
    public static final String KEY_AWAY_TEAM = "AwayTeam";
    public static final String KEY_HOME_SCORES = "HomeScores";
    public static final String KEY_AWAY_SCORES = "AwayScores";
    public static final String KEY_IS_PLAYED = "IsPlayed";
    private static final String TAG = "DBAdapter";

    private static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME;
    public static String DATABASE_TABLE;
    public static String login;
    public static String DATABASE_CREATE;

    private Context context = null;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DatabaseResults(Context context, String login) {
        this.login = login;
        this.context = context;

        this.DATABASE_NAME = "ResultsOf" + login;
        this.DATABASE_TABLE = "Results" + login;
        this.DATABASE_CREATE = "create table "
                + DATABASE_TABLE
                + " (_id integer primary key autoincrement, "
                + KEY_DATE +", "
                + KEY_HOME_TEAM +", "
                + KEY_AWAY_TEAM +", "
                + KEY_HOME_SCORES +", "
                + KEY_AWAY_SCORES +", "
                + KEY_IS_PLAYED +
                ")";
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private SQLiteDatabase db;
        private String login;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("DatabaseResults: " + DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public void open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
    }

    public void close()
    {
        DBHelper.close();
    }

    public long createMatch(String date, String homeTeam, String awayTeam)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_HOME_TEAM, homeTeam);
        initialValues.put(KEY_AWAY_TEAM, awayTeam);
        initialValues.put(KEY_HOME_SCORES, "");
        initialValues.put(KEY_AWAY_SCORES, "");
        initialValues.put(KEY_IS_PLAYED, "no");
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public long playMatch(String homeTeam, int homeScores, String awayTeam, int awayScores)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_HOME_SCORES, homeScores);
        initialValues.put(KEY_AWAY_SCORES, awayScores);
        initialValues.put(KEY_IS_PLAYED, "yes");
        return db.update(DATABASE_TABLE, initialValues, KEY_HOME_TEAM + "=? AND " + KEY_AWAY_TEAM +"=?", new String[] {homeTeam, awayTeam});
    }

    public Cursor getAllResults(){
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        return cursor;
    }

    public int getSpecifiedResultHomeScore(String homeTeam, String awayTeam) throws SQLException {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_HOME_TEAM + "=? AND " + KEY_AWAY_TEAM + "=?" , new String[] {homeTeam, awayTeam});
        cursor.moveToFirst();
        int homeScore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_HOME_SCORES)));
        return homeScore;
    }

    public int getSpecifiedResultAwayScore(String homeTeam, String awayTeam) throws SQLException {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_HOME_TEAM + "=? AND " + KEY_AWAY_TEAM + "=?" , new String[] {homeTeam, awayTeam});
        cursor.moveToFirst();
        int awayScore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_AWAY_SCORES)));
        return awayScore;
    }

    public int getPointsOfTeam(String name){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_HOME_TEAM + "=? AND " + KEY_IS_PLAYED + " =?" , new String[] {name, "yes"});
        cursor.moveToFirst();
        int result = 0;
        for (int a = 0; a < cursor.getCount(); a ++){
            int homeScore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_HOME_SCORES)));
            int awayScore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_AWAY_SCORES)));
            if (homeScore > awayScore){
                result = result + 3;
            } else {
                if (homeScore == awayScore){
                    result = result + 1;
                }
            }
            cursor.moveToNext();
        }

        Cursor cursor1 = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_AWAY_TEAM + "=? AND " + KEY_IS_PLAYED + " =?" , new String[] {name, "yes"});
        cursor1.moveToFirst();
        for (int a = 0; a < cursor1.getCount(); a ++){
            int homeScore = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(KEY_HOME_SCORES)));
            int awayScore = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(KEY_AWAY_SCORES)));
            if (awayScore > homeScore){
                result = result + 3;
            } else {
                if (homeScore == awayScore){
                    result = result + 1;
                }
            }
            cursor1.moveToNext();
        }

        return result;
    }

    public int getScoredGoalsOfTeam(String name){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_HOME_TEAM + "=? AND " + KEY_IS_PLAYED + " =?" , new String[] {name, "yes"});
        cursor.moveToFirst();
        int result = 0;
        for (int a = 0; a < cursor.getCount(); a ++){
            int homeScore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_HOME_SCORES)));
            result = result + homeScore;
            cursor.moveToNext();
        }

        Cursor cursor1 = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_AWAY_TEAM + "=? AND " + KEY_IS_PLAYED + " =?" , new String[] {name, "yes"});
        cursor1.moveToFirst();
        for (int a = 0; a < cursor1.getCount(); a ++){
            int awayScore = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(KEY_AWAY_SCORES)));
            result = result + awayScore;
            cursor1.moveToNext();
        }

        return result;
    }

    public int getLostGoalsOfTeam(String name){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_HOME_TEAM + "=? AND " + KEY_IS_PLAYED + " =?" , new String[] {name, "yes"});
        cursor.moveToFirst();
        int result = 0;
        for (int a = 0; a < cursor.getCount(); a ++){
            int awayScore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_AWAY_SCORES)));
            result = result + awayScore;
            cursor.moveToNext();
        }

        Cursor cursor1 = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_AWAY_TEAM + "=? AND " + KEY_IS_PLAYED + " =?" , new String[] {name, "yes"});
        cursor1.moveToFirst();
        for (int a = 0; a < cursor1.getCount(); a ++){
            int homeScore = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(KEY_HOME_SCORES)));
            result = result + homeScore;
            cursor1.moveToNext();
        }

        return result;
    }

    public int getMatchesAmount(String name){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_HOME_TEAM + "=? AND " + KEY_IS_PLAYED + " =?" , new String[] {name, "yes"});
        cursor.moveToFirst();
        int result = 0;
        for (int a = 0; a < cursor.getCount(); a ++){
            result = result + 1;
            cursor.moveToNext();
        }

        Cursor cursor1 = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_AWAY_TEAM + "=? AND " + KEY_IS_PLAYED + " =?" , new String[] {name, "yes"});
        cursor1.moveToFirst();
        for (int a = 0; a < cursor1.getCount(); a ++){
            result = result + 1;
            cursor1.moveToNext();
        }

        return result;
    }

    public long setRoundOfMatch(String date, String homeTeam, String awayTeam){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, date);
                return db.update(DATABASE_TABLE, initialValues, KEY_HOME_TEAM + "=? AND "
                        + KEY_AWAY_TEAM + " =?", new String[] {homeTeam, awayTeam});
    }

    public Cursor getRoundMatches(int round, int match){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_DATE + "=?" , new String[] {String.valueOf(round)});
        cursor.moveToPosition(match);
        return cursor;
    }
}