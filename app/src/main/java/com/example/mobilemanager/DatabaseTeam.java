package com.example.mobilemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseTeam {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NUMBER = "ContarctNumber";
    public static final String KEY_NAME = "Name";
    public static final String KEY_POSITION = "Position";
    public static final String KEY_GK_SKILLS = "GoalkeepingSkills";
    public static final String KEY_DEF_SKILLS = "DefenceSkills";
    public static final String KEY_ATT_SKILLS = "AttackSkills";
    public static final String KEY_CONTRACT_WAGE = "Wage";
    public static final String KEY_VALUE = "Value";


    private static final String TAG = "DBAdapter";

    private static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME;
    public static String DATABASE_TABLE;
    public static String login;
    public static String DATABASE_CREATE;

    private Context context = null;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DatabaseTeam(Context context, String login) {
        this.login = login;
        this.context = context;

        this.DATABASE_NAME = "PlayersTeamOf" + login;
        this.DATABASE_TABLE = "PlayersInClub" + login;
        this.DATABASE_CREATE = "create table "
                + DATABASE_TABLE
                + " (_id integer primary key autoincrement, "
                + KEY_NUMBER + ", "
                + KEY_NAME + ", "
                + KEY_POSITION + ", "
                + KEY_GK_SKILLS + ", "
                + KEY_DEF_SKILLS + ", "
                + KEY_ATT_SKILLS + ", "
                + KEY_CONTRACT_WAGE + ", "
                + KEY_VALUE +
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

    public long createPlayer(int number, String name, String position, int gkSkills, int defSkills, int attSkills, int wage, int value)

    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NUMBER, String.valueOf(number));
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_POSITION, position);
        initialValues.put(KEY_GK_SKILLS, String.valueOf(gkSkills));
        initialValues.put(KEY_DEF_SKILLS, String.valueOf(defSkills));
        initialValues.put(KEY_ATT_SKILLS, String.valueOf(attSkills));
        initialValues.put(KEY_CONTRACT_WAGE, String.valueOf(wage));
        initialValues.put(KEY_VALUE, String.valueOf(value));
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public Cursor getAllPlayers(){
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor getSpecificGroupOfPlayers(String groupName){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_POSITION + "=?", new String[] {groupName});
        cursor.moveToFirst();
        return cursor;
    }

}