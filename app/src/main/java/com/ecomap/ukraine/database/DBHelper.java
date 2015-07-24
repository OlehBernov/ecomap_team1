package com.ecomap.ukraine.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Oleh on 7/24/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Problems.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.CREATE_PROBLEMS_TABLE);
        db.execSQL(DBContract.CREATE_DETAILS_TABLE);
        db.execSQL(DBContract.CREATE_ACTIVITY_TABLE);
        db.execSQL(DBContract.CREATE_PHOTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.DELETE_ALL_TABLES);
        onCreate(db);
    }
}
