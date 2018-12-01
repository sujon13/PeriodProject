package com.example.sujon4002.periodproject.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;

    // All Static variables
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = Config.DATABASE_NAME;

    // Constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tables SQL execution

        String CREATE_PERIOD_TABLE = "CREATE TABLE " + Config.TABLE_PERIOD_INFORMATION + "("
                + Config.COLUMN_PERIOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_PERIOD_START_DATE + " TEXT NOT NULL, "
                + Config.COLUMN_PERIOD_END_DATE + " TEXT NOT NULL, "
                + Config.COLUMN_PERIOD_DESCRIPTION+ " TEXT " //nullable
                + ")";
        //Logger.d("Table create SQL: " + CREATE_PERSONAL_TABLE);

        db.execSQL(CREATE_PERIOD_TABLE);
        //Logger.d("DB created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_PERIOD_INFORMATION);

        // Create tables again
        onCreate(db);
    }

}