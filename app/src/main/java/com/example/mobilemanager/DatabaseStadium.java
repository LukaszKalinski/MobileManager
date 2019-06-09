    package com.example.mobilemanager;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;

    public class DatabaseStadium {
        public static final String KEY_ROWID = "_id";
        public static final String KEY_NAME = "BuildingName";
        public static final String KEY_LEVEL = "CurrentLevel";
        public static final String KEY_UPGRADE_COST = "NextLevelCost";
        public static final String KEY_FIRST_VALUE = "FirstValue";
        public static final String KEY_SECOND_VALUE = "SecondValue";
        public static final String KEY_THIRD_VALUE = "ThirdValue";
        private static final String TAG = "DBAdapter";

        private static final int DATABASE_VERSION = 1;
        public static String DATABASE_NAME;
        public static String DATABASE_TABLE;
        public static String login;
        public static String DATABASE_CREATE;

        private Context context = null;
        private DatabaseHelper DBHelper;
        private SQLiteDatabase db;

        public DatabaseStadium (Context context, String login) {
            this.login = login;
            this.context = context;

            this.DATABASE_NAME = "StadiumOf" + login;
            this.DATABASE_TABLE = "Stadium" + login;
            this.DATABASE_CREATE = "create table "
                    + DATABASE_TABLE
                    + " (_id integer primary key autoincrement, "
                    + KEY_NAME + ", "
                    + KEY_LEVEL + ", "
                    + KEY_UPGRADE_COST + ", "
                    + KEY_FIRST_VALUE + ", "
                    + KEY_SECOND_VALUE + ", "
                    + KEY_THIRD_VALUE +
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
        System.out.println("DatabaseStadium: " + DATABASE_CREATE);
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

    public void open() throws SQLException{
        db = DBHelper.getWritableDatabase();
    }

    public void close(){
        DBHelper.close();
    }

    public long createBuilding(String name, int currentLevel, int currentCost, int firstValue, int secondValue, int thirdValue){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_LEVEL, String.valueOf(currentLevel));
        initialValues.put(KEY_UPGRADE_COST, String.valueOf(currentCost));
        initialValues.put(KEY_FIRST_VALUE, firstValue);
        initialValues.put(KEY_SECOND_VALUE, secondValue);
        initialValues.put(KEY_THIRD_VALUE, thirdValue);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public long updateBuilding(String name, int currentLevel, int currentCost, int firstValue, int secondValue, int thirdValue){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LEVEL, String.valueOf(currentLevel + 1));
        initialValues.put(KEY_UPGRADE_COST, String.valueOf(currentCost));
        initialValues.put(KEY_FIRST_VALUE, firstValue);
        initialValues.put(KEY_SECOND_VALUE, secondValue);
        initialValues.put(KEY_THIRD_VALUE, thirdValue);
        return db.update(DATABASE_TABLE, initialValues, KEY_NAME + "=?", new String[] {name});
    }

    public Cursor getAllBuildings(){
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor getSpecificBuilding(String name){
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE +
                " WHERE " + KEY_NAME + "=?", new String[] {name});
        cursor.moveToFirst();
        return cursor;
    }

}