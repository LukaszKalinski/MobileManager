package com.example.mobilemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseTeams {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_LOGIN = "Username";
    public static final String KEY_CLUB_NAME = "ClubName";
    public static final String KEY_CLUB_ATTACK_POWER = "ClubAttackPower";
    public static final String KEY_CLUB_DEFENCE_POWER = "ClubDefencePower";
    private static final String TAG = "DBAdapter";

    private static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME;
    public static String DATABASE_TABLE;
    public static String login;
    public static String DATABASE_CREATE;

    private Context context = null;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DatabaseTeams(Context context, String login) {
        this.login = login;
        this.context = context;

        this.DATABASE_NAME = "TeamOf" + login;
        this.DATABASE_TABLE = "ClubTeam" + login;
        this.DATABASE_CREATE = "create table "
                + DATABASE_TABLE
                + " (_id integer primary key autoincrement, "
                + KEY_LOGIN + ", "
                + KEY_CLUB_NAME + ", "
                + KEY_CLUB_ATTACK_POWER + ", "
                + KEY_CLUB_DEFENCE_POWER +
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

    public long createTeams(String userName, int attackPower, int defencePower)

    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LOGIN, "Player " + userName);
        initialValues.put(KEY_CLUB_NAME, "Club " + userName);
        initialValues.put(KEY_CLUB_ATTACK_POWER, String.valueOf(attackPower));
        initialValues.put(KEY_CLUB_DEFENCE_POWER, String.valueOf(defencePower));
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public Cursor getAllClubs(){
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        return cursor;
    }

    public String getMyClubName() throws SQLException {
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();
        String myClub = cursor.getString(cursor.getColumnIndex(KEY_CLUB_NAME));
        cursor.close();
        return myClub;
    }

    public String getClubName(int id) throws SQLException {
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        cursor.moveToPosition(id);
        String selectedClub = cursor.getString(cursor.getColumnIndex(KEY_CLUB_NAME));
        cursor.close();
        return selectedClub;
    }

    public int getAttackClubPower(int id) throws SQLException {
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        cursor.moveToPosition(id);
        int selectedClub = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CLUB_ATTACK_POWER)));
        cursor.close();
        return selectedClub;
    }

    public int getDefenceClubPower(int id) throws SQLException {
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        cursor.moveToPosition(id);
        int selectedClub = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CLUB_DEFENCE_POWER)));
        cursor.close();
        return selectedClub;
    }

    public long setClubPower(String team, int attackPower, int defencePower)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CLUB_ATTACK_POWER, attackPower);
        initialValues.put(KEY_CLUB_DEFENCE_POWER, defencePower);
        return db.update(DATABASE_TABLE, initialValues, KEY_CLUB_NAME + "=?", new String[] {team});
    }

    public int getClubIdByName(String name) throws SQLException {
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        int id;
        int aMax = cursor.getCount();
        for (int a = 0; a < aMax; a ++){
            cursor.moveToPosition(a);
            if (cursor.getString(2).equals(name)){
                id = cursor.getPosition();
                break;
            }
        }
        id = cursor.getPosition();
        cursor.close();
        return id;
    }
}

