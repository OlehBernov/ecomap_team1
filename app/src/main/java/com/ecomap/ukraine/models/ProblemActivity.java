package com.ecomap.ukraine.models;

import android.database.Cursor;

import com.ecomap.ukraine.database.DBContract;

/**
 * Class ProblemActivity represent information about activity that are related to the problem.
 * Activity - some action which is related to the problem (like, comment, creation).
 * Created by Andriy on 17.07.2015.
 */
public class ProblemActivity {

    /**
     * id of the problem
     */
    private int problemId;

    /**
     * id of current problemActivity
     */
    private int problemActivityId;

    /**
     * represend type of current problemActivity
     */
    private int activityTypesId;

    /**
     * id of user who added this problemActivity
     */
    private int userId;

    /**
     * contains text of current problemActivity
     */
    private String content;

    /**
     * contains adding date of current problemActivity
     */
    private String date;

    /**
     * name of user who added this problemActivity
     */
    private String userName;

    /**
     * returns problem id
     * @return problem id
     */
    public int getProblemId() {
        return problemId;
    }

    /**
     * provides access to position
     */
    public int getProblemActivityId() {
        return problemActivityId;
    }

    /**
     * provides access to position
     */
    public int getActivityTypesId() {
        return activityTypesId;
    }


    /**
     * provides access to userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * provides access to content
     */
    public String getContent() {
        return content;
    }

    /**
     * provides access to date
     */
    public String getDate() {
        return date;
    }

    /**
     * provides access to userName
     */
    public String getFirstName() {
        return userName;
    }

    /**
     * Constructor of class
     * @param problemActivityId id of current problemActivity
     * @param activityTypesId represend type of current problemActivity
     * @param userId id of user who added this problemActivity
     * @param content contains text of current problemActivity
     * @param date contains adding date of current problemActivity
     * @param userName name of user who added this problemActivity
     */
    public ProblemActivity(int probemId, int problemActivityId, int activityTypesId, int userId,
                           String content, String date, String userName) {
        this.problemId = probemId;
        this.problemActivityId = problemActivityId;
        this.activityTypesId = activityTypesId;
        this.userId = userId;
        this.content = content;
        this.date = date;
        this.userName = userName;
    }

    public ProblemActivity(Cursor cursor, int problemId) {
        this.problemId = problemId;
        this.problemActivityId = cursor.getInt(cursor.getColumnIndex(DBContract.ProblemActivity
                                                                    .PROBLEM_ACTIVITY_ID));
        this.activityTypesId = cursor.getInt(cursor.getColumnIndex(DBContract.ProblemActivity
                                                                    .ACTIVITY_TYPES_ID));
        this.userId = cursor.getInt(cursor.getColumnIndex(DBContract.ProblemActivity
                                                                     .ACTIVITY_USERS_ID));
        this.content = cursor.getString(cursor.getColumnIndex(DBContract.ProblemActivity
                                                                        .PROBLEM_ACTIVITY_CONTENT));
        this.date = cursor.getString(cursor.getColumnIndex(DBContract.ProblemActivity
                                                                      .PROBLEM_ACTIVITY_DATE));
        this.userName = cursor.getString(cursor.getColumnIndex(DBContract.ProblemActivity
                                                                          .USER_NAME));

    }
}