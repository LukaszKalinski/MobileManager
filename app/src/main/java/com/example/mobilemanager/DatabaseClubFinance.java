package com.example.mobilemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseClubFinance {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_LOGIN = "Username";
    public static final String KEY_BALANCE = "AccountBalance";
    public static final String KEY_TRANSFER_BUDGET = "TransferBudget";
    public static final String KEY_WAGE_BUDGET = "WageBudget";
    public static final String KEY_CURRENT_WAGE_TOTAL = "CurrentWageTotal";
    public static final String KEY_MAX_WAGE = "MaximumWage";
    public static final String KEY_CURRENT_MAX_WAGE = "CurrentMaxWage";
    private static final String TAG = "DBAdapter";

    private static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME;
    public static String DATABASE_TABLE;
    public static String login;
    public static String DATABASE_CREATE;

    private Context context = null;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DatabaseClubFinance(Context context, String login) {
        this.login = login;
        this.context = context;

        this.DATABASE_NAME = "ClubFinanceOf" + login;
        this.DATABASE_TABLE = "CLubFinance" + login;
        this.DATABASE_CREATE = "create table "
                + DATABASE_TABLE
                + " (_id integer primary key autoincrement, "
                + KEY_LOGIN +", "
                + KEY_BALANCE +", "
                + KEY_TRANSFER_BUDGET +", "
                + KEY_WAGE_BUDGET +", "
                + KEY_CURRENT_WAGE_TOTAL +", "
                + KEY_MAX_WAGE +", "
                + KEY_CURRENT_MAX_WAGE +
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
            System.out.println("DatabaseTeams: " + DATABASE_CREATE);
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

    public long firstAddition(String userName)

    {
        ContentValues initialValues = new ContentValues();
        double accountBalance = 1000000;
        double transferBudget = 0.8 * accountBalance;
        double wageBudget = 0.1*(0.8*accountBalance);
        double maxPossWage = 0.1*(0.8*accountBalance)/8;
        initialValues.put(KEY_LOGIN, userName);
        initialValues.put(KEY_BALANCE, String.valueOf(accountBalance));
        initialValues.put(KEY_TRANSFER_BUDGET, String.valueOf(transferBudget));
        initialValues.put(KEY_WAGE_BUDGET, String.valueOf(wageBudget));
        initialValues.put(KEY_MAX_WAGE, String.valueOf(maxPossWage));
        initialValues.put(KEY_CURRENT_WAGE_TOTAL, String.valueOf(0));
        initialValues.put(KEY_CURRENT_MAX_WAGE, String.valueOf(0));
        return db.insert(DATABASE_TABLE, null, initialValues);

    }

    public Cursor getRecords(){
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        return cursor;
    }

    public void refreshClubFinance(String name, double balanceIncome, double currentWageTotal, double currentMaxWage){
        String[] names = new String[] {name};
        ContentValues newFinance = new ContentValues();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_LOGIN + "=?", names);
        mCursor.moveToFirst();
        double accountBalance = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(KEY_BALANCE)));
        if (balanceIncome > 0){
            newFinance.put(KEY_BALANCE, String.valueOf(accountBalance + balanceIncome));
            newFinance.put(KEY_TRANSFER_BUDGET, String.valueOf(0.8*(accountBalance + balanceIncome)));
            newFinance.put(KEY_WAGE_BUDGET, String.valueOf(0.1*(0.8*(accountBalance + balanceIncome))));
            newFinance.put(KEY_MAX_WAGE, String.valueOf(0.1*(0.8*(accountBalance + balanceIncome))/8));
        }
        if (currentWageTotal > 0 ){newFinance.put(KEY_CURRENT_WAGE_TOTAL, String.valueOf(currentWageTotal));}
        if (currentMaxWage > 0 ){newFinance.put(KEY_CURRENT_MAX_WAGE, String.valueOf(currentMaxWage));}

        db.update(DATABASE_TABLE, newFinance, KEY_LOGIN + "=?", names);
    }

    public double getAccountBalance(String name) throws SQLException {
        String[] names = new String[] {name};
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_LOGIN + "=?", names);
        mCursor.moveToFirst();
        double accountBalance = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(KEY_BALANCE)));
        return accountBalance;
    }

    public double getTransferBudget(String name) throws SQLException {
        String[] names = new String[] {name};
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_LOGIN + "=?", names);
        mCursor.moveToFirst();
        double transferBudget = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(KEY_TRANSFER_BUDGET)));
        return transferBudget;
    }

    public double getWageBudget(String name) throws SQLException {
        String[] names = new String[] {name};
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_LOGIN + "=?", names);
        mCursor.moveToFirst();
        double wageBudget = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(KEY_WAGE_BUDGET)));
        return wageBudget;
    }

    public double getCurrentWages(String name) throws SQLException {
        String[] names = new String[] {name};
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_LOGIN + "=?", names);
        mCursor.moveToFirst();
        double currentWages = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(KEY_CURRENT_WAGE_TOTAL)));
        return currentWages;
    }

    public double getMaxPossWage(String name) throws SQLException {
        String[] names = new String[] {name};
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_LOGIN + "=?", names);
        mCursor.moveToFirst();
        double maxPossWage = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(KEY_MAX_WAGE)));
        return maxPossWage;
    }

    public double getCurrentMaxWage(String name) throws SQLException {
        String[] names = new String[] {name};
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_LOGIN + "=?", names);
        mCursor.moveToFirst();
        double currentMaxWage = Double.valueOf(mCursor.getString(mCursor.getColumnIndex(KEY_CURRENT_MAX_WAGE)));
        return currentMaxWage;
    }

}




