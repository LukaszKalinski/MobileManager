package com.example.mobilemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseFinance {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_LOGIN = "Username";
    public static final String KEY_BALANCE = "AccountBalance";
    private static final String TAG = "DBAdapter";

    private static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME;
    public static String DATABASE_TABLE;
    public static String login;
    public static String DATABASE_CREATE;

    private Context context = null;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DatabaseFinance(Context context, String login) {
        this.login = login;
        this.context = context;

        this.DATABASE_NAME = "FinanceOf" + login;
        this.DATABASE_TABLE = "Finance" + login;
        this.DATABASE_CREATE = "create table "
                + DATABASE_TABLE
                + " (_id integer primary key autoincrement, "
                + KEY_LOGIN +", "
                + KEY_BALANCE +")";
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
            System.out.println("DatabaseFinance: " + DATABASE_CREATE);
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

    public long firstAdd(String userName)

    {
        ContentValues initialValues = new ContentValues();
        int accountBalance = 1000000;
        initialValues.put(KEY_LOGIN, userName);
        initialValues.put(KEY_BALANCE, String.valueOf(accountBalance));
        return db.insert(DATABASE_TABLE, null, initialValues);

    }

    public Cursor getRecords(){
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        return cursor;
    }

    public void refreshAccountBalance(String name, int currentAmount, int addedAmount){
        String[] names = new String[] {name};
        ContentValues newAccountBalance = new ContentValues();
        newAccountBalance.put(KEY_BALANCE, String.valueOf(currentAmount + addedAmount));
        db.update(DATABASE_TABLE, newAccountBalance, KEY_LOGIN + "=?", names);
    }

    public int getAccountBalance(String name) throws SQLException {
        String[] names = new String[] {name};
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_LOGIN + "=?", names);
        mCursor.moveToFirst();
        int accountBalance = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(KEY_BALANCE)));
        return accountBalance;
    }

}




