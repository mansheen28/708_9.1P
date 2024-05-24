package com.example.lostfound;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lostFoundDB.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTable = 
                "CREATE TABLE items (" +
                        "item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "item_name TEXT NOT NULL, " +
                        "contact_number TEXT NOT NULL, " +
                        "item_description TEXT, " +
                        "item_location TEXT, " +
                        "item_type TEXT, " +
                        "date_found TEXT)";
        db.execSQL(sqlCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS items");
        onCreate(db);
    }
}
