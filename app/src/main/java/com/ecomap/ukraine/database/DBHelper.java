package com.ecomap.ukraine.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ecomap.ukraine.data.manager.DataListener;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.ProblemActivity;
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
    private static final int DB_VERSION = 2;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ", ";
    private static final String CREATE_ACTIVITIES_TABLE
            = "CREATE TABLE " + DBContract.ProblemActivity.TABLE_NAME + " (" +
            DBContract.ProblemActivity.PROBLEM_ACTIVITY_ID + INT_TYPE + COMMA_SEP +
            DBContract.ProblemActivity.PROBLEM_ACTIVITY_DATE + INT_TYPE + COMMA_SEP +
            DBContract.ProblemActivity.PROBLEM_ID + INT_TYPE + COMMA_SEP + DBContract.ProblemActivity
            .ACTIVITY_TYPES_ID + INT_TYPE + COMMA_SEP + DBContract.ProblemActivity
            .USER_NAME + TEXT_TYPE + COMMA_SEP + DBContract.ProblemActivity.ACTIVITY_USERS_ID
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
            + INT_TYPE + COMMA_SEP + DBContract.Details.TITLE + TEXT_TYPE + COMMA_SEP +
            DBContract.Details.PROBLEM_CONTENT + TEXT_TYPE +
            COMMA_SEP + DBContract.Details.PROPOSAL + TEXT_TYPE + COMMA_SEP +
            DBContract.Details.MODERATION + INT_TYPE + COMMA_SEP + DBContract.Details.SEVERITY +
            INT_TYPE + COMMA_SEP + DBContract.Details.VOTES + INT_TYPE + COMMA_SEP +
            DBContract.Details.LAST_UPDATE + TEXT_TYPE + ")";
    private static final String CREATE_PROBLEMS_TABLE
            = "CREATE TABLE " + DBContract.Problems.TABLE_NAME + " (" + DBContract.Problems.ID +
            " INTEGER PRIMARY KEY, " + DBContract.Problems.PROBLEM_STATUS + INT_TYPE +
            COMMA_SEP + DBContract.Problems.PROBLEM_TYPES_ID + INT_TYPE + COMMA_SEP +
            DBContract.Problems.PROBLEM_DATE + INT_TYPE + COMMA_SEP + DBContract.Problems.LATITUDE +
            REAL_TYPE + COMMA_SEP + DBContract.Problems.LONGITUDE + REAL_TYPE + ")";

    private static final String DELETE_FROM = "DELETE FROM ";

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
                //  this.addAllProblems(requestResult);
                break;
            case RequestTypes.PROBLEM_DETAIL:
                SQLiteDatabase db = this.getWritableDatabase();
                String delete = DELETE_FROM + DBContract.Details.TABLE_NAME ;
                db.execSQL(delete);
                delete = DELETE_FROM + DBContract.Photos.TABLE_NAME;
                db.execSQL(delete);
                delete = DELETE_FROM + DBContract.ProblemActivity.TABLE_NAME;
                db.execSQL(delete);
                this.setProblemDetails((Details) requestResult);
                break;
        }
    }

    public void setProblemDetails(Details details) {
        SQLiteDatabase db = this.getWritableDatabase();

        String delete = DELETE_FROM + DBContract.Details.TABLE_NAME ;
        db.execSQL(delete);
        delete = DELETE_FROM + DBContract.Photos.TABLE_NAME;
        db.execSQL(delete);
        delete = DELETE_FROM + DBContract.ProblemActivity.TABLE_NAME;
        db.execSQL(delete);

        ContentValues values = new ContentValues();

        values.put(DBContract.Details.PROBLEM_ID, details.getProblemId());
        values.put(DBContract.Details.PROBLEM_CONTENT, details.getContent());
        values.put(DBContract.Details.PROPOSAL, details.getProposal());
        values.put(DBContract.Details.TITLE, details.getTitle());
        values.put(DBContract.Details.MODERATION, details.getModerations());
        values.put(DBContract.Details.SEVERITY, details.getSeverity());
        values.put(DBContract.Details.VOTES, details.getVotes());
        values.put(DBContract.Details.SEVERITY, details.getSeverity());
        values.put(DBContract.Details.LAST_UPDATE, details.getLastUpdate());

        db.insert(DBContract.Details.TABLE_NAME, null, values);

        List<ProblemActivity> problemActivities = details.getProblemActivities();
        setProblemActivities(problemActivities);
        List<Photo> photos = details.getPhotos();
        setPhotos(photos);
    }

    private void setProblemActivities(List<ProblemActivity> problemActivities) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values;
        for (ProblemActivity problemActivity: problemActivities) {
            values = new ContentValues();

            values.put(DBContract.ProblemActivity.PROBLEM_ACTIVITY_ID, problemActivity.getProblemActivityId());
            values.put(DBContract.ProblemActivity.PROBLEM_ACTIVITY_DATE, problemActivity.getDate());
            values.put(DBContract.ProblemActivity.PROBLEM_ID, problemActivity.getProblemId());
            values.put(DBContract.ProblemActivity.ACTIVITY_TYPES_ID, problemActivity.getActivityTypesId());
            values.put(DBContract.ProblemActivity.USER_NAME, problemActivity.getFirstName());
            values.put(DBContract.ProblemActivity.ACTIVITY_USERS_ID, problemActivity.getUserId());
            values.put(DBContract.ProblemActivity.PROBLEM_ACTIVITY_CONTENT, problemActivity.getContent());

            db.insert(DBContract.ProblemActivity.TABLE_NAME, null, values);
        }
    }

    private void setPhotos(List<Photo> photos) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values;
        for (Photo photo: photos) {
            values = new ContentValues();

            values.put(DBContract.Photos.PROBLEM_ID, photo.getProblemId());
            values.put(DBContract.Photos.PHOTO_ID, photo.getPhotoId());
            values.put(DBContract.Photos.PHOTO_USERS_ID, photo.getUserId());
            values.put(DBContract.Photos.PHOTO_STATUS, photo.getStatus());
            values.put(DBContract.Photos.LINK, photo.getLink());
            values.put(DBContract.Photos.PHOTO_DESCRIPTION, photo.getDescription());

        db.insert(DBContract.Photos.TABLE_NAME, null, values);
        }
    }

    public Details getProblemDetails(int problemId) {

        SQLiteDatabase db = this.getWritableDatabase();

        List<Photo> photos = getProblemPhotos(problemId);
        List<ProblemActivity> problemActivities = getProblemActivities(problemId);

        String[] projection = {
                DBContract.Details.PROBLEM_CONTENT,
                DBContract.Details.PROPOSAL,
                DBContract.Details.TITLE,
                DBContract.Details.MODERATION,
                DBContract.Details.SEVERITY,
                DBContract.Details.VOTES,
                DBContract.Details.LAST_UPDATE
        };

        String selection = DBContract.Details.PROBLEM_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(problemId)};

        Cursor cursor = db.query(
                DBContract.Details.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        if (cursor == null) {
            return null;
        }

        Details details = new Details(
                problemId,
                cursor.getInt(cursor.getColumnIndex(DBContract.Details.SEVERITY)),
                cursor.getInt(cursor.getColumnIndex(DBContract.Details.MODERATION)),
                cursor.getInt(cursor.getColumnIndex(DBContract.Details.VOTES)),
                cursor.getString(cursor.getColumnIndex(DBContract.Details.PROBLEM_CONTENT)),
                cursor.getString(cursor.getColumnIndex(DBContract.Details.PROPOSAL)),
                cursor.getString(cursor.getColumnIndex(DBContract.Details.TITLE)),
                problemActivities,
                photos,
                cursor.getString(cursor.getColumnIndex(DBContract.Details.LAST_UPDATE))
        );

        cursor.close();

        return details;
    }

    private List<Photo> getProblemPhotos(int problemId) {

        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {
                DBContract.Photos.PHOTO_ID,
                DBContract.Photos.PHOTO_USERS_ID,
                DBContract.Photos.PHOTO_STATUS,
                DBContract.Photos.LINK,
                DBContract.Photos.PHOTO_DESCRIPTION,
        };

        String selection = DBContract.Photos.PROBLEM_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(problemId)};

        Cursor cursor = db.query(
                DBContract.Photos.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor == null) {
            return null;
        }

        List<Photo> photos = new ArrayList<Photo>();

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Photo photo = new Photo(
                    problemId,
                    cursor.getInt(cursor.getColumnIndex(DBContract.Photos.PHOTO_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.Photos.PHOTO_USERS_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.Photos.PHOTO_STATUS)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Photos.LINK)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Photos.PHOTO_DESCRIPTION))
                    );

            photos.add(photo);
        }

        cursor.close();

        return photos;
    }

    private List<ProblemActivity> getProblemActivities(int problemId) {

        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {
                DBContract.ProblemActivity.PROBLEM_ACTIVITY_DATE,
                DBContract.ProblemActivity.ACTIVITY_TYPES_ID,
                DBContract.ProblemActivity.PROBLEM_ACTIVITY_ID,
                DBContract.ProblemActivity.USER_NAME,
                DBContract.ProblemActivity.ACTIVITY_USERS_ID,
                DBContract.ProblemActivity.PROBLEM_ACTIVITY_CONTENT,
        };

        String selection = DBContract.Photos.PROBLEM_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(problemId)};

        Cursor cursor = db.query(
                DBContract.ProblemActivity.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor == null) {
            return null;
        }

        List<ProblemActivity> problemActivities = new ArrayList<ProblemActivity>();

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            ProblemActivity problemActivity = new ProblemActivity(
                    problemId,
                    cursor.getInt(cursor.getColumnIndex(DBContract.ProblemActivity.PROBLEM_ACTIVITY_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.ProblemActivity.ACTIVITY_TYPES_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.ProblemActivity.ACTIVITY_USERS_ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.ProblemActivity.PROBLEM_ACTIVITY_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(DBContract.ProblemActivity.PROBLEM_ACTIVITY_DATE)),
                    cursor.getString(cursor.getColumnIndex(DBContract.ProblemActivity.USER_NAME))
            );

            problemActivities.add(problemActivity);
        }

        cursor.close();

        return problemActivities;
    }

}
