package com.ecomap.ukraine.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseIntArray;

import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemActivity;
import com.ecomap.ukraine.models.Statistics;
import com.ecomap.ukraine.models.Top10Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exposes methods to work with inner database.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DELETE_ACTIVITIES_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.ProblemActivity.TABLE_NAME;
    private static final String DELETE_PHOTOS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.Photos.TABLE_NAME;
    private static final String DELETE_DETAILS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.Details.TABLE_NAME;
    private static final String DELETE_PROBLEMS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.Problems.TABLE_NAME;
    private static final String DELETE_TOP_BY_VOTES_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.TopByVotes.TABLE_NAME;
    private static final String DELETE_TOP_BY_COMMENTS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.TopByComments.TABLE_NAME;
    private static final String DELETE_TOP_BY_SEVERITY_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.TopBySeverity.TABLE_NAME;
    private static final String DELETE_STATISTICS_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.Statistics.TABLE_NAME;

    private static final String DB_NAME = "Problems.db";
    private static final int DB_VERSION = 4;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ", ";

    private static final String CREATE_ACTIVITIES_TABLE
            = "CREATE TABLE " + DBContract.ProblemActivity.TABLE_NAME + " (" +
            DBContract.ProblemActivity.PROBLEM_ACTIVITY_ID + INT_TYPE + COMMA_SEP +
            DBContract.ProblemActivity.PROBLEM_ACTIVITY_DATE + INT_TYPE + COMMA_SEP +
            DBContract.ProblemActivity.PROBLEM_ID + INT_TYPE + COMMA_SEP +
            DBContract.ProblemActivity.ACTIVITY_TYPES_ID + INT_TYPE + COMMA_SEP +
            DBContract.ProblemActivity.USER_NAME + TEXT_TYPE + COMMA_SEP +
            DBContract.ProblemActivity.ACTIVITY_USERS_ID + INT_TYPE + COMMA_SEP +
            DBContract.ProblemActivity.PROBLEM_ACTIVITY_CONTENT
            + TEXT_TYPE + ")";

    private static final String CREATE_PHOTOS_TABLE
            = "CREATE TABLE " + DBContract.Photos.TABLE_NAME + " (" +
            DBContract.Photos.PROBLEM_ID + INT_TYPE + COMMA_SEP +
            DBContract.Photos.PHOTO_ID + INT_TYPE + COMMA_SEP +
            DBContract.Photos.PHOTO_STATUS + INT_TYPE + COMMA_SEP +
            DBContract.Photos.LINK + TEXT_TYPE + COMMA_SEP +
            DBContract.Photos.PHOTO_DESCRIPTION + TEXT_TYPE + ")";

    private static final String CREATE_DETAILS_TABLE
            = "CREATE TABLE " + DBContract.Details.TABLE_NAME + " (" +
            DBContract.Details.PROBLEM_ID + INT_TYPE + COMMA_SEP +
            DBContract.Details.TITLE + TEXT_TYPE + COMMA_SEP +
            DBContract.Details.PROBLEM_CONTENT + TEXT_TYPE + COMMA_SEP +
            DBContract.Details.PROPOSAL + TEXT_TYPE + COMMA_SEP +
            DBContract.Details.MODERATION + INT_TYPE + COMMA_SEP +
            DBContract.Details.SEVERITY + INT_TYPE + COMMA_SEP +
            DBContract.Details.VOTES + INT_TYPE + COMMA_SEP +
            DBContract.Details.LAST_UPDATE + TEXT_TYPE + ")";

    private static final String CREATE_PROBLEMS_TABLE
            = "CREATE TABLE " + DBContract.Problems.TABLE_NAME + " (" +
            DBContract.Problems.ID + " INTEGER PRIMARY KEY, " +
            DBContract.Problems.PROBLEM_STATUS + INT_TYPE + COMMA_SEP +
            DBContract.Problems.PROBLEM_TYPES_ID + INT_TYPE + COMMA_SEP +
            DBContract.Problems.PROBLEM_TITLE + TEXT_TYPE + COMMA_SEP +
            DBContract.Problems.PROBLEM_DATE + TEXT_TYPE + COMMA_SEP +
            DBContract.Problems.LATITUDE + REAL_TYPE + COMMA_SEP +
            DBContract.Problems.LONGITUDE + REAL_TYPE + ")";

    private static final String CREATE_TOP_BY_VOTES_TABLE
            = "CREATE TABLE " + DBContract.TopByVotes.TABLE_NAME + " (" +
            DBContract.Top.PROBLEM_ID + INT_TYPE + COMMA_SEP +
            DBContract.Top.PROBLEM_TITLE + TEXT_TYPE + COMMA_SEP +
            DBContract.TopByVotes.PROBLEM_VOTES + INT_TYPE + ")";

    private static final String CREATE_TOP_BY_COMMENTS_TABLE
            = "CREATE TABLE " + DBContract.TopByComments.TABLE_NAME + " (" +
            DBContract.Top.PROBLEM_ID + INT_TYPE + COMMA_SEP +
            DBContract.Top.PROBLEM_TITLE + TEXT_TYPE + COMMA_SEP +
            DBContract.TopByComments.PROBLEM_COMMENTS + INT_TYPE + ")";

    private static final String CREATE_TOP_BY_SEVERITY_TABLE
            = "CREATE TABLE " + DBContract.TopBySeverity.TABLE_NAME + " (" +
            DBContract.Top.PROBLEM_ID + INT_TYPE + COMMA_SEP +
            DBContract.Top.PROBLEM_TITLE + TEXT_TYPE + COMMA_SEP +
            DBContract.TopBySeverity.PROBLEM_SEVERITY + INT_TYPE + ")";

    private static final String CREATE_STATISTICS
            = "CREATE TABLE " + DBContract.Statistics.TABLE_NAME + " (" +
            DBContract.Statistics.PROBLEM_TYPE_ID + INT_TYPE + COMMA_SEP +
            DBContract.Statistics.DAILY_VALUE + INT_TYPE + COMMA_SEP +
            DBContract.Statistics.WEEKLY_VALUE + INT_TYPE + COMMA_SEP +
            DBContract.Statistics.MONTH_VALUE + INT_TYPE + COMMA_SEP +
            DBContract.Statistics.ANNUAL_VALUE + INT_TYPE + COMMA_SEP +
            DBContract.Statistics.VALUE_FOR_ALL_TIME + INT_TYPE + ")";

    private static final String DELETE_FROM = "DELETE FROM ";

    public DBHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROBLEMS_TABLE);
        db.execSQL(CREATE_DETAILS_TABLE);
        db.execSQL(CREATE_ACTIVITIES_TABLE);
        db.execSQL(CREATE_PHOTOS_TABLE);

        db.execSQL(CREATE_TOP_BY_VOTES_TABLE);
        db.execSQL(CREATE_TOP_BY_COMMENTS_TABLE);
        db.execSQL(CREATE_TOP_BY_SEVERITY_TABLE);

        db.execSQL(CREATE_STATISTICS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_PHOTOS_TABLE);
        db.execSQL(DELETE_DETAILS_TABLE);
        db.execSQL(DELETE_ACTIVITIES_TABLE);
        db.execSQL(DELETE_PROBLEMS_TABLE);

        db.execSQL(DELETE_TOP_BY_VOTES_TABLE);
        db.execSQL(DELETE_TOP_BY_COMMENTS_TABLE);
        db.execSQL(DELETE_TOP_BY_SEVERITY_TABLE);

        db.execSQL(DELETE_STATISTICS_TABLE);

        onCreate(db);
    }

    /**
     * Performs updating database table, which contains brief
     * information about all problems.
     *
     * @param problems new list of all problems.
     */
    public void updateAllProblems(final List<Problem> problems) {
        if (problems == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DBHelper.DELETE_FROM + DBContract.Problems.TABLE_NAME);
        setAllProblems(problems);
    }

    /**
     * Performs updating database row, which contains
     * information about details of concrete problem.
     *
     * @param details new details of concrete problem.
     */
    public void updateProblemDetails(final Details details) {
        if (details == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        int problemId = details.getProblemId();
        db.delete(DBContract.Details.TABLE_NAME,
                DBContract.Details.PROBLEM_ID + " = ?",
                new String[]{"" + problemId});
        db.delete(DBContract.Photos.TABLE_NAME,
                DBContract.Photos.PROBLEM_ID + " = ?",
                new String[]{"" + problemId});
        db.delete(DBContract.ProblemActivity.TABLE_NAME,
                DBContract.ProblemActivity.PROBLEM_ID + " = ?",
                new String[]{"" + problemId});
        setProblemDetails(details);
    }

    public void updateTop10(final AllTop10Items allTop10Items) {
        if (allTop10Items == null) {
            return;
        }

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DBHelper.DELETE_FROM + DBContract.TopByVotes.TABLE_NAME);
        db.execSQL(DBHelper.DELETE_FROM + DBContract.TopByComments.TABLE_NAME);
        db.execSQL(DBHelper.DELETE_FROM + DBContract.TopBySeverity.TABLE_NAME);

        setTop10ByType(allTop10Items.getMostLikedProblems(), DBContract.TopByVotes.TABLE_NAME,
                       DBContract.TopByVotes.PROBLEM_VOTES);
        setTop10ByType(allTop10Items.getMostPopularProblems(),DBContract.TopByComments.TABLE_NAME,
                       DBContract.TopByComments.PROBLEM_COMMENTS);
        setTop10ByType(allTop10Items.getMostImportantProblems(), DBContract.TopBySeverity.TABLE_NAME,
                       DBContract.TopBySeverity.PROBLEM_SEVERITY);
    }

    /**
     * Performs updating database table, which contains
     * information about statistics.
     *
     * @param statistics new problems statistics.
     */
    public void updateStatistics(final Statistics statistics) {
        if (statistics == null) {
            return;
        }

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DBHelper.DELETE_FROM + DBContract.Statistics.TABLE_NAME);

        ContentValues contentValues = new ContentValues();
        for (int i = 1; i < (statistics.size() + 1); i++) {
            contentValues.put(DBContract.Statistics.PROBLEM_TYPE_ID, i);
            contentValues.put(DBContract.Statistics.DAILY_VALUE, statistics.getDailyStatistics().get(i));
            contentValues.put(DBContract.Statistics.WEEKLY_VALUE, statistics.getWeeklyStatistics().get(i));
            contentValues.put(DBContract.Statistics.MONTH_VALUE, statistics.getMonthStatistics().get(i));
            contentValues.put(DBContract.Statistics.ANNUAL_VALUE, statistics.getAnnualStatistics().get(i));
            contentValues.put(DBContract.Statistics.VALUE_FOR_ALL_TIME, statistics.getStatisticsForAllTime().get(i));
            db.insert(DBContract.Statistics.TABLE_NAME, null, contentValues);
            contentValues.clear();
        }
    }

    /**
     * Adds problem details into the database.
     *
     * @param details the details to add to the database.
     */
    public void setProblemDetails(final Details details) {
        if (details == null) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DBContract.Details.PROBLEM_ID, details.getProblemId());
        values.put(DBContract.Details.PROBLEM_CONTENT, details.getContent());
        values.put(DBContract.Details.PROPOSAL, details.getProposal());
        values.put(DBContract.Details.TITLE, details.getTitle());
        values.put(DBContract.Details.MODERATION, details.getModeration());
        values.put(DBContract.Details.SEVERITY, details.getSeverity());
        values.put(DBContract.Details.VOTES, details.getVotes());
        values.put(DBContract.Details.SEVERITY, details.getSeverity());
        values.put(DBContract.Details.LAST_UPDATE, details.getLastUpdate());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(DBContract.Details.TABLE_NAME, null, values);

        List<ProblemActivity> problemActivities = details.getProblemActivities();
        if (problemActivities != null) {
            setProblemActivities(problemActivities);
        }

        List<Photo> photos = details.getPhotos();
        setPhotos(photos);
    }

    /**
     * Returns list of all problems that are currently in the database.
     *
     * @return list of problems.
     */
    public List<Problem> getAllProblems() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(DBContract.Problems.TABLE_NAME, null, null, null, null, null, null);
        List<Problem> problems = null;
        if (cursor.moveToFirst()) {
            problems = buildAllProblemList(cursor);
        }
        return problems;
    }

    /**
     * Sets brief information about all problems into the database.
     *
     * @param problems server response to all problems request.
     */
    public void setAllProblems(final List<Problem> problems) {
        if (problems == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (Problem problem : problems) {
            contentValues.put(DBContract.Problems.ID, problem.getProblemId());
            contentValues.put(DBContract.Problems.PROBLEM_STATUS, problem.getStatus());
            contentValues.put(DBContract.Problems.PROBLEM_TYPES_ID, problem.getProblemType());
            contentValues.put(DBContract.Problems.PROBLEM_TITLE, problem.getTitle());
            contentValues.put(DBContract.Problems.PROBLEM_DATE, problem.getDate());
            contentValues.put(DBContract.Problems.LATITUDE, problem.getPosition().latitude);
            contentValues.put(DBContract.Problems.LONGITUDE, problem.getPosition().longitude);
            db.insert(DBContract.Problems.TABLE_NAME, null, contentValues);
            contentValues.clear();
        }
    }

    /**
     * Returns problem details that are currently in the database.
     *
     * @param problemId id of required problem.
     * @return problem details.
     */
    public Details getProblemDetails(final int problemId) {
        if (problemId < 0) {
            return null;
        }

        List<Photo> photos = getProblemPhotos(problemId);
        List<ProblemActivity> problemActivities = getProblemActivities(problemId);

        String[] projection = {
                DBContract.Details.PROBLEM_CONTENT, DBContract.Details.PROPOSAL,
                DBContract.Details.TITLE, DBContract.Details.MODERATION,
                DBContract.Details.SEVERITY, DBContract.Details.VOTES,
                DBContract.Details.LAST_UPDATE
        };
        String selection = DBContract.Details.PROBLEM_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(problemId)};

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(DBContract.Details.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        Details details = null;
        if (cursor.moveToFirst()) {
            details = buildProblemDetails(problemId, cursor, problemActivities, photos);
        }

        return details;
    }

    /**
     * Returns time of the last onAllProblemsUpdate of the information
     * about concrete problem.
     *
     * @param problemId id of required problem.
     * @return time of the last onAllProblemsUpdate.
     */
    public String getLastUpdateTime(final int problemId) {
        if (problemId < 0) {
            return null;
        }

        SQLiteDatabase db = getWritableDatabase();
        String[] projection = {DBContract.Details.LAST_UPDATE};
        String selection = DBContract.Details.PROBLEM_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(problemId)};

        Cursor cursor = db.query(DBContract.Details.TABLE_NAME, projection,
                selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(DBContract.Details.LAST_UPDATE));
        } else {
            return null;
        }
    }

    public AllTop10Items getAllTop10Items() {
        AllTop10Items allTop10Items = new
                AllTop10Items(getTop10ByComments(), getTop10BySeverity(), getTop10ByVotes());
        if (allTop10Items.getMostLikedProblems() == null) {
            return null;
        }
        return allTop10Items;
    }

    public List<Top10Item> getTop10ByVotes() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(DBContract.TopByVotes.TABLE_NAME, null, null, null, null, null, null);
        List<Top10Item> top10ItemList = null;
        if (cursor.moveToFirst()) {
            top10ItemList = buildTop10byVotesList(cursor);
        }
        return top10ItemList;
    }

    public void setTop10ByType(final List<Top10Item> top10ByVotes, final String tableName,
                               final String typeOfTop) {
        if (top10ByVotes == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < top10ByVotes.size(); i++) {
            contentValues.put(DBContract.Top.PROBLEM_ID, top10ByVotes.get(i).getProblemID());
            contentValues.put(DBContract.Top.PROBLEM_TITLE, top10ByVotes.get(i).getTitle());
            contentValues.put(typeOfTop, top10ByVotes.get(i).getValue());
            db.insert(tableName, null, contentValues);
            contentValues.clear();
        }
    }

    public List<Top10Item> getTop10ByComments() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(DBContract.TopByComments.TABLE_NAME, null, null, null, null, null, null);
        List<Top10Item> top10ItemList = null;
        if (cursor.moveToFirst()) {
            top10ItemList = buildTop10byCommentList(cursor);
        }
        return top10ItemList;
    }

    public List<Top10Item> getTop10BySeverity() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(DBContract.TopBySeverity.TABLE_NAME, null, null, null, null, null, null);
        List<Top10Item> top10ItemList = null;
        if (cursor.moveToFirst()) {
            top10ItemList = buildTop10bySeverityList(cursor);
        }
        return top10ItemList;
    }

    /**
     * Returns statistics that are currently in the database.
     *
     * @return saved statistics.
     */
    public Statistics getStatistics() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(DBContract.Statistics.TABLE_NAME, null, null, null, null, null, null);

        SparseIntArray dailyStatistics = new SparseIntArray();
        SparseIntArray weeklyStatistics = new SparseIntArray();
        SparseIntArray monthStatistics = new SparseIntArray();
        SparseIntArray annualStatistics = new SparseIntArray();
        SparseIntArray statisticsForAllTime = new SparseIntArray();

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            dailyStatistics.append(i, cursor.getInt(cursor.getColumnIndex(DBContract.Statistics.DAILY_VALUE)));
            weeklyStatistics.append(i, cursor.getInt(cursor.getColumnIndex(DBContract.Statistics.WEEKLY_VALUE)));
            monthStatistics.append(i, cursor.getInt(cursor.getColumnIndex(DBContract.Statistics.MONTH_VALUE)));
            annualStatistics.append(i, cursor.getInt(cursor.getColumnIndex(DBContract.Statistics.ANNUAL_VALUE)));
            statisticsForAllTime.append(i, cursor.getInt(cursor.getColumnIndex(DBContract.Statistics.VALUE_FOR_ALL_TIME)));
            cursor.moveToNext();
        }

        Map<String, SparseIntArray> statisticItems = new HashMap<>();
        statisticItems.put(Statistics.DAILY, dailyStatistics);
        statisticItems.put(Statistics.WEEKLY, weeklyStatistics);
        statisticItems.put(Statistics.MONTH, monthStatistics);
        statisticItems.put(Statistics.ANNUAL, annualStatistics);
        statisticItems.put(Statistics.FOR_ALL_TIME, statisticsForAllTime);

        return new Statistics(statisticItems);
    }

    /**
     * Sets activities of concrete problem into the database.
     *
     * @param problemActivities activities of the problem.
     */
    private void setProblemActivities(final List<ProblemActivity> problemActivities) {
        if (problemActivities == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values;
        for (ProblemActivity problemActivity : problemActivities) {

            values = new ContentValues();
            values.put(DBContract.ProblemActivity.PROBLEM_ACTIVITY_ID,
                    problemActivity.getProblemActivityId());
            values.put(DBContract.ProblemActivity.PROBLEM_ACTIVITY_DATE,
                    problemActivity.getDate());
            values.put(DBContract.ProblemActivity.PROBLEM_ID,
                    problemActivity.getProblemId());
            values.put(DBContract.ProblemActivity.ACTIVITY_TYPES_ID,
                    problemActivity.getActivityType());
            values.put(DBContract.ProblemActivity.USER_NAME,
                    problemActivity.getFirstName());
            values.put(DBContract.ProblemActivity.ACTIVITY_USERS_ID,
                    problemActivity.getUserId());
            values.put(DBContract.ProblemActivity.PROBLEM_ACTIVITY_CONTENT,
                    problemActivity.getContent());

            db.insert(DBContract.ProblemActivity.TABLE_NAME, null, values);
            values.clear();
        }
    }

    /**
     * Sets photos of a concrete problem into the database.
     *
     * @param photos photos of a problem.
     */
    private void setPhotos(final List<Photo> photos) {
        if (photos == null) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values;
        for (Photo photo : photos) {

            values = new ContentValues();
            values.put(DBContract.Photos.PROBLEM_ID, photo.getProblemId());
            values.put(DBContract.Photos.PHOTO_ID, photo.getPhotoId());
            values.put(DBContract.Photos.PHOTO_STATUS, photo.getStatus());
            values.put(DBContract.Photos.LINK, photo.getLink());
            values.put(DBContract.Photos.PHOTO_DESCRIPTION, photo.getDescription());

            db.insert(DBContract.Photos.TABLE_NAME, null, values);
            values.clear();
        }
    }

    /**
     * Returns photos of concrete problem
     * that are currently in the database.
     *
     * @param problemId id of the required problem.
     * @return map of photos.
     */
    private List<Photo> getProblemPhotos(final int problemId) {
        if (problemId < 0) {
            return null;
        }
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                DBContract.Photos.PHOTO_ID, DBContract.Photos.PHOTO_STATUS,
                DBContract.Photos.LINK, DBContract.Photos.PHOTO_DESCRIPTION,
        };
        String selection = DBContract.Photos.PROBLEM_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(problemId)};

        Cursor cursor = db.query(DBContract.Photos.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        List<Photo> photoList = null;
        if (cursor.moveToFirst()) {
            photoList = buildPhotosList(problemId, cursor);
        }

        return photoList;
    }

    /**
     * Returns list of all problem activities that
     * are currently in the database.
     *
     * @param problemId id of the required problem.
     * @return list of problem activities.
     */
    private List<ProblemActivity> getProblemActivities(final int problemId) {
        if (problemId < 0) {
            return null;
        }

        SQLiteDatabase db = getWritableDatabase();
        String[] projection = {
                DBContract.ProblemActivity.PROBLEM_ACTIVITY_DATE,
                DBContract.ProblemActivity.ACTIVITY_TYPES_ID,
                DBContract.ProblemActivity.PROBLEM_ACTIVITY_ID,
                DBContract.ProblemActivity.USER_NAME,
                DBContract.ProblemActivity.ACTIVITY_USERS_ID,
                DBContract.ProblemActivity.PROBLEM_ACTIVITY_CONTENT,
        };
        String selection = DBContract.Photos.PROBLEM_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(problemId)};

        Cursor cursor = db.query(DBContract.ProblemActivity.TABLE_NAME, projection,
                selection, selectionArgs, null, null, null);

        List<ProblemActivity> problemActivities = null;
        if (cursor.moveToFirst()) {
            problemActivities = buildProblemActivitiesList(problemId, cursor);
        }

        return problemActivities;
    }

    private List<Top10Item> buildTop10byVotesList(final Cursor cursor) {
        List<Top10Item> top10ItemList = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            int problemID = cursor.getInt(cursor.getColumnIndex(DBContract.Top.
                    PROBLEM_ID));
            String problemTitle = cursor.getString(cursor.getColumnIndex(DBContract.Top.
                    PROBLEM_TITLE));
            int problemVotes = cursor.getInt(cursor.getColumnIndex(DBContract.TopByVotes.
                    PROBLEM_VOTES));
            Top10Item top10Item = new Top10Item(problemID, problemTitle, problemVotes);
            top10ItemList.add(top10Item);
            cursor.moveToNext();
        }
        return top10ItemList;
    }

    private List<Top10Item> buildTop10byCommentList(final Cursor cursor) {
        List<Top10Item> top10ItemList = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            int problemID = cursor.getInt(cursor.getColumnIndex(DBContract.Top.
                    PROBLEM_ID));
            String problemTitle = cursor.getString(cursor.getColumnIndex(DBContract.Top.
                    PROBLEM_TITLE));
            int problemComments = cursor.getInt(cursor.getColumnIndex(DBContract.TopByComments.
                    PROBLEM_COMMENTS));
            Top10Item top10Item = new Top10Item(problemID, problemTitle, problemComments);
            top10ItemList.add(top10Item);
            cursor.moveToNext();
        }
        return top10ItemList;
    }

    private List<Top10Item> buildTop10bySeverityList(final Cursor cursor) {
        List<Top10Item> top10ItemList = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            int problemID = cursor.getInt(cursor.getColumnIndex(DBContract.Top.
                    PROBLEM_ID));
            String problemTitle = cursor.getString(cursor.getColumnIndex(DBContract.Top.
                    PROBLEM_TITLE));
            int problemVotes = cursor.getInt(cursor.getColumnIndex(DBContract.TopBySeverity.
                    PROBLEM_SEVERITY));
            Top10Item top10Item = new Top10Item(problemID, problemTitle, problemVotes);
            top10ItemList.add(top10Item);
            cursor.moveToNext();
        }
        return top10ItemList;
    }

    /**
     * Builds list of all problems.
     *
     * @param cursor contains query result.
     * @return list of all problems.
     */
    private List<Problem> buildAllProblemList(final Cursor cursor) {
        List<Problem> problems = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            int problemStatusId = cursor.getInt(cursor.getColumnIndex(DBContract
                    .Problems
                    .PROBLEM_STATUS));
            int problemTypeId = cursor.getInt(cursor.getColumnIndex(DBContract
                    .Problems
                    .PROBLEM_TYPES_ID));

            Problem problem = new Problem(
                    cursor.getInt(cursor.getColumnIndex(DBContract.Problems.ID)),
                    problemStatusId,
                    problemTypeId,
                    cursor.getString(cursor.getColumnIndex(DBContract.Problems.PROBLEM_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Problems.PROBLEM_DATE)),
                    cursor.getDouble(cursor.getColumnIndex(DBContract.Problems.LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(DBContract.Problems.LONGITUDE))
            );
            problems.add(problem);
            cursor.moveToNext();
        }

        return problems;
    }

    /**
     * Builds problem details object.
     *
     * @param problemId         id of the problem.
     * @param cursor            contains query result.
     * @param problemActivities activities of the problem.
     * @param photos            photos of a problem.
     * @return constructed Details object.
     */
    private Details buildProblemDetails(final int problemId, final Cursor cursor,
                                        final List<ProblemActivity> problemActivities,
                                        final List<Photo> photos) {
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

    /**
     * Builds map of photos of a problem.
     *
     * @param problemId id of the problem.
     * @param cursor    contains query result.
     * @return map of photos.
     */
    private List<Photo> buildPhotosList(final int problemId, final Cursor cursor) {
        List<Photo> photos = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Photo photo = new Photo(
                    problemId,
                    cursor.getInt(cursor.getColumnIndex(DBContract.Photos.PHOTO_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.Photos.PHOTO_STATUS)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Photos.LINK)),
                    cursor.getString(cursor.getColumnIndex(DBContract.Photos.PHOTO_DESCRIPTION))
            );
            photos.add(photo);
            cursor.moveToNext();
        }
        cursor.close();

        return photos;
    }

    /**
     * Builds list of problem activities.
     *
     * @param problemId id of a problem.
     * @param cursor    contains query result.
     * @return list of problem activities.
     */
    private List<ProblemActivity> buildProblemActivitiesList(final int problemId,
                                                             final Cursor cursor) {
        List<ProblemActivity> problemActivities = new ArrayList<>();
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
            cursor.moveToNext();
        }
        cursor.close();

        return problemActivities;
    }

}

