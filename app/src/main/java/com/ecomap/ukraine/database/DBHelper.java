package com.ecomap.ukraine.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.ecomap.ukraine.R;

import com.ecomap.ukraine.data.manager.DataListener;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemActivity;
import com.ecomap.ukraine.updating.serverclient.RequestTypes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exposes methods to work with inner database.
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
            DBContract.Problems.PROBLEM_TITLE + TEXT_TYPE + COMMA_SEP + DBContract.Problems.PROBLEM_DATE +
            TEXT_TYPE + COMMA_SEP + DBContract.Problems.LATITUDE +
            REAL_TYPE + COMMA_SEP + DBContract.Problems.LONGITUDE + REAL_TYPE + ")";

    private static final String DELETE_FROM = "DELETE FROM ";

    /**
     * Context of the application.
     */
    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
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

    /**
     * Performs updating database when server response comes.
     *
     * @param requestType the type of request handled.
     * @param requestResult the result of request.
     */
    @Override
    public void update(int requestType, Object requestResult) {
        if (requestResult == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        switch (requestType) {
            case RequestTypes.ALL_PROBLEMS:
                db.execSQL(DBHelper.DELETE_FROM + DBContract.Problems.TABLE_NAME);
                this.addAllProblems(requestResult);
                break;
            case RequestTypes.PROBLEM_DETAIL:
                db = this.getWritableDatabase();
                db.execSQL(DELETE_FROM + DBContract.Details.TABLE_NAME);
                db.execSQL(DELETE_FROM + DBContract.Photos.TABLE_NAME);
                db.execSQL(DELETE_FROM + DBContract.ProblemActivity.TABLE_NAME);
                this.setProblemDetails((Details) requestResult);
                break;
        }
        db.close();
    }

    /**
     * Sets brief information about all problems into the database.
     *
     * @param requestResult server response to all problems request.
     */
    public void addAllProblems(Object requestResult){
        if (requestResult == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        List<Problem> problems = (ArrayList)requestResult;
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
        saveTime();
    }

    /**
     * Returns list of all problems that are currently in the database.
     *
     * @return list of problems.
     */
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

    /**
     * Adds problem details into the database.
     *
     * @param details the details to add to the database.
     */
    public void setProblemDetails(Details details) {
        if (details == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
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
        db.close();

        List<ProblemActivity> problemActivities = details.getProblemActivities();
        setProblemActivities(problemActivities);
        Map<Photo, Bitmap> photos = details.getPhotos();
        setPhotos(photos);
    }

    /**
     * Sets photos of a concrete problem into the database.
     *
     * @param photos photos of a problem.
     */
    private void setPhotos(Map<Photo, Bitmap> photos) {
        if (photos == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values;
        for (Photo photo: photos.keySet()) {
            writeToFile(photos.get(photo), photo.getLink());

            values = new ContentValues();

            values.put(DBContract.Photos.PROBLEM_ID, photo.getProblemId());
            values.put(DBContract.Photos.PHOTO_ID, photo.getPhotoId());
            values.put(DBContract.Photos.PHOTO_USERS_ID, photo.getUserId());
            values.put(DBContract.Photos.PHOTO_STATUS, photo.getStatus());
            values.put(DBContract.Photos.LINK, photo.getLink());
            values.put(DBContract.Photos.PHOTO_DESCRIPTION, photo.getDescription());

        db.insert(DBContract.Photos.TABLE_NAME, null, values);
        }
        db.close();
    }

    private void writeToFile(Bitmap bitmap, String name) {
        FileOutputStream outputStream = null;
        try {
            String path = context.getFilesDir().getPath();
            outputStream = new FileOutputStream(
                    new File(path + "/" + name));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns problem details that are currently in the database.
     *
     * @param problemId id of required problem.
     * @return problem details.
     */
    public Details getProblemDetails(int problemId) {
        if (problemId < 0) {
            return null;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Map<Photo, Bitmap> photos = getProblemPhotos(problemId);
        List<ProblemActivity> problemActivities = getProblemActivities(problemId);

        String[] projection = {
                DBContract.Details.PROBLEM_CONTENT, DBContract.Details.PROPOSAL,
                DBContract.Details.TITLE, DBContract.Details.MODERATION,
                DBContract.Details.SEVERITY, DBContract.Details.VOTES,
                DBContract.Details.LAST_UPDATE
        };
        String selection = DBContract.Details.PROBLEM_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(problemId)};
        Cursor cursor = db.query(DBContract.Details.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);
        db.close();

        if (cursor == null) {
            return null;
        }

        return buildProblemDetails(problemId, cursor, problemActivities, photos);
    }

    /**
     * Returns time of the last update of the information
     * about concrete problem.
     *
     * @param problemId id of required problem.
     * @return time of the last update.
     */
    public String getLastUpdateTime(int problemId) {
        if (problemId < 0) {
            return null;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {DBContract.Details.LAST_UPDATE};
        String selection = DBContract.Photos.PROBLEM_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(problemId)};

        Cursor cursor = db.query(DBContract.ProblemActivity.TABLE_NAME, projection,
                selection, selectionArgs, null, null, null);
        db.close();

        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(DBContract.Details.LAST_UPDATE));
    }

    /**
     * Returns photos of concrete problem
     * that are currently in the database.
     *
     * @param problemId id of the required problem.
     * @return map of photos.
     */
    private Map<Photo, Bitmap> getProblemPhotos(int problemId) {
        if (problemId < 0) {
            return null;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {
                DBContract.Photos.PHOTO_ID, DBContract.Photos.PHOTO_USERS_ID,
                DBContract.Photos.PHOTO_STATUS, DBContract.Photos.LINK,
                DBContract.Photos.PHOTO_DESCRIPTION,
        };
        String selection = DBContract.Photos.PROBLEM_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(problemId)};

        Cursor cursor = db.query(DBContract.Photos.TABLE_NAME, projection, selection,
                                 selectionArgs, null, null, null);
        db.close();

        if (cursor == null) {
            return null;
        }

        return buildPhotosMap(problemId, cursor);
    }

    private Bitmap getBitmapByName(String fileName) {
        FileInputStream inputStream = null;
        try {
            String path = context.getFilesDir().getPath();
            inputStream = new FileInputStream(new File(path + "/" + fileName));
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            return BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.photo_error1);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns list of all problem activities that
     * are currently in the database.
     *
     * @param problemId id of the required problem.
     * @return list of problem activities.
     */
    private List<ProblemActivity> getProblemActivities(int problemId) {
        if (problemId < 0) {
            return null;
        }

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

        Cursor cursor = db.query(DBContract.ProblemActivity.TABLE_NAME, projection,
                                 selection, selectionArgs, null, null, null);
        db.close();

        if (cursor == null) {
            return null;
        }

        return buildProblemActivitiesList(problemId, cursor);
    }

    /**
     * Sets acivitiew of concrete problem into the database.
     *
     * @param problemActivities activities of the problem.
     */
    private void setProblemActivities(List<ProblemActivity> problemActivities) {
        if (problemActivities == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        for (ProblemActivity problemActivity: problemActivities) {
            values = new ContentValues();
            values.put(DBContract.ProblemActivity.PROBLEM_ACTIVITY_ID,
                       problemActivity.getProblemActivityId());
            values.put(DBContract.ProblemActivity.PROBLEM_ACTIVITY_DATE,
                       problemActivity.getDate());
            values.put(DBContract.ProblemActivity.PROBLEM_ID,
                       problemActivity.getProblemId());
            values.put(DBContract.ProblemActivity.ACTIVITY_TYPES_ID,
                       problemActivity.getActivityTypesId());
            values.put(DBContract.ProblemActivity.USER_NAME,
                       problemActivity.getFirstName());
            values.put(DBContract.ProblemActivity.ACTIVITY_USERS_ID,
                       problemActivity.getUserId());
            values.put(DBContract.ProblemActivity.PROBLEM_ACTIVITY_CONTENT,
                       problemActivity.getContent());

            db.insert(DBContract.ProblemActivity.TABLE_NAME, null, values);
        }
        db.close();
    }

    /**
     * Builds problem details object.
     *
     * @param problemId id of the problem.
     * @param cursor contains query result.
     * @param problemActivities activities of the problem.
     * @param photos photos of a problem.
     * @return constructed Details object.
     */
    private Details buildProblemDetails(int problemId, Cursor cursor,
                                        List<ProblemActivity> problemActivities,
                                        Map<Photo, Bitmap> photos) {
        cursor.moveToFirst();
        Details details = new Details(cursor, problemId, problemActivities, photos);
        cursor.close();

        return details;
    }

    /**
     * Builds map of photos of a problem.
     *
     * @param problemId id of the problem.
     * @param cursor contains query result.
     * @return map of photos.
     */
    private Map<Photo, Bitmap> buildPhotosMap(int problemId, Cursor cursor) {
        Map<Photo, Bitmap> photos = new HashMap<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Photo photo = new Photo(cursor, problemId);
            Bitmap image = getBitmapByName(photo.getLink());
            photos.put(photo, null);
        }
        cursor.close();

        return photos;
    }

    /**
     * Builds list of problem activities.
     *
     * @param problemId id of a problem.
     * @param cursor contains query result.
     * @return list of problem activities.
     */
    private List<ProblemActivity> buildProblemActivitiesList(int problemId,
                                                             Cursor cursor) {
        List<ProblemActivity> problemActivities = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            ProblemActivity problemActivity = new ProblemActivity(cursor, problemId);
            problemActivities.add(problemActivity);
        }
        cursor.close();

        return problemActivities;
    }

    /**
     * Saves time of the last database update
     * to the SharedPreferences.
     */
    private void saveTime() {
        SharedPreferences settings = context.getSharedPreferences(DBContract.Problems.TIME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(DBContract.Problems.TIME, System.currentTimeMillis());
        editor.commit();
    }
}
