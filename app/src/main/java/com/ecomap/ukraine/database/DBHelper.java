package com.ecomap.ukraine.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ecomap.ukraine.data.manager.DataListener;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.updating.serverclient.RequestTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleh on 7/24/2015.
 */
public class DBHelper extends SQLiteOpenHelper implements DataListener {
    private static final String DELETE_ACTIVITIES_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.ProblemActivity.TABLE_NAME;
    private static final String DELETE_PHOTOS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.Photos.TABLE_NAME;
    private static final String DELETE_DETAILS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.Details.TABLE_NAME;
    private static final String DELETE_PROBLEMS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.Problems.TABLE_NAME;
    //
    private static final String DB_NAME = "Problems.db";
    private static final int DB_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ", ";
    private static final String CREATE_ACTIVITIES_TABLE
            = "CREATE TABLE " + DBContract.ProblemActivity.TABLE_NAME + " (" +
            DBContract.ProblemActivity.PROBLEM_ACTIVITY_DATE + INT_TYPE + COMMA_SEP +
            DBContract.ProblemActivity.PROBLEM_ID + INT_TYPE + COMMA_SEP + DBContract.ProblemActivity
            .ACTIVITY_TYPES_ID + INT_TYPE + COMMA_SEP + DBContract.ProblemActivity
            .USER_NAME + TEXT_TYPE + COMMA_SEP + DBContract.ProblemActivity.COMMENT_USERS_ID
            + INT_TYPE + COMMA_SEP + DBContract.ProblemActivity.PROBLEM_ACTIVITY_CONTENT
            + TEXT_TYPE + ")";
    private static final String CREATE_PHOTOS_TABLE
            = "CREATE TABLE " + DBContract.Photos.TABLE_NAME + " (" + DBContract.Photos.PROBLEM_ID +
            INT_TYPE + COMMA_SEP + DBContract.Photos.PHOTO_ID + INT_TYPE + COMMA_SEP +
            DBContract.Photos.PHOTO_USERS_ID + INT_TYPE + COMMA_SEP + DBContract.Photos.PHOTO_STATUS
            + INT_TYPE + COMMA_SEP + DBContract.Photos.LINK + TEXT_TYPE + COMMA_SEP +
            DBContract.Photos.PHOTO_DESCRIPTION + TEXT_TYPE + ")";
    private static final String CREATE_DETAILS_TABLE
            = "CREATE TABLE " + DBContract.Details.TABLE_NAME + " (" + DBContract.Details.PROBLEM_ID
            + INT_TYPE + COMMA_SEP + DBContract.Details.PROBLEM_CONTENT + TEXT_TYPE +
            COMMA_SEP + DBContract.Details.PROPOSAL + TEXT_TYPE + COMMA_SEP +
            DBContract.Details.MODERATION + INT_TYPE + COMMA_SEP + DBContract.Details.SEVERITY +
            INT_TYPE + COMMA_SEP + DBContract.Details.VOTES + INT_TYPE + COMMA_SEP +
            DBContract.Details.LAST_UPDATE + TEXT_TYPE + ")";
    private static final String CREATE_PROBLEMS_TABLE
            = "CREATE TABLE " + DBContract.Problems.TABLE_NAME + " (" + DBContract.Problems.ID +
            " INTEGER PRIMARY KEY, " + DBContract.Problems.PROBLEM_STATUS + INT_TYPE +
            COMMA_SEP + DBContract.Problems.PROBLEM_TYPES_ID + INT_TYPE + COMMA_SEP +
            DBContract.Problems.PROBLEM_TITLE + TEXT_TYPE + COMMA_SEP + DBContract.Problems.PROBLEM_DATE +
            TEXT_TYPE + COMMA_SEP + DBContract.Problems.LATITUDE +
            REAL_TYPE + COMMA_SEP + DBContract.Problems.LONGITUDE + REAL_TYPE + ")";
    private static final String TRUNCATE_FROM = "TRUNCATE FROM ";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROBLEMS_TABLE);
        db.execSQL(CREATE_DETAILS_TABLE);
        db.execSQL(CREATE_ACTIVITIES_TABLE);
        db.execSQL(CREATE_PHOTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_PHOTOS_TABLE);
        db.execSQL(DELETE_DETAILS_TABLE);
        db.execSQL(DELETE_ACTIVITIES_TABLE);
        db.execSQL(DELETE_PROBLEMS_TABLE);
        onCreate(db);
    }

    @Override
    public void update(int requestType, Object requestResult) {
        switch (requestType) {
            case RequestTypes.ALL_PROBLEMS:
                this.addAllProblems(requestResult);
                break;
        }
    }

    private void addAllProblems(Object requestResult){
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery(DBHelper.TRUNCATE_FROM + DBContract.Problems.TABLE_NAME, null);
        ContentValues contentValues = new ContentValues();
        List<Problem> problems = (List)requestResult;
        for (Problem problem : problems) {
            contentValues.put(DBContract.Problems.ID, problem.getProblemId());
            contentValues.put(DBContract.Problems.PROBLEM_STATUS, problem.getStatusId());
            contentValues.put(DBContract.Problems.PROBLEM_TYPES_ID, problem.getProblemTypesId());
            contentValues.put(DBContract.Problems.PROBLEM_TITLE, problem.getTitle());
            contentValues.put(DBContract.Problems.PROBLEM_DATE, problem.getDate());
            contentValues.put(DBContract.Problems.LATITUDE, problem.getPosition().latitude);
            contentValues.put(DBContract.Problems.LONGITUDE, problem.getPosition().longitude);
            db.insert(DBContract.Problems.TABLE_NAME, null, contentValues);
            contentValues.clear();
        }
        db.close();
    }

    public List<Problem> getAllProblems () {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DBContract.Problems.TABLE_NAME, null, null, null, null, null, null);
        List<Problem> problems = new ArrayList<Problem>();
        if (cursor.moveToFirst()) {
            do {
                Problem problem = new Problem(cursor);
                problems.add(problem);
            } while (cursor.moveToNext());
        }
        db.close();
        return problems;
    }

}


