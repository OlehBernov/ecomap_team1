package com.ecomap.ukraine.models;

/**
 * Class ProblemActivity represent information about activity that are related to the problem.
 * Activity - some action which is related to the problem (like, comment, creation).
 * Created by Andriy on 17.07.2015.
 */
public class ProblemActivity {

    //id of current problemActivity
    private int problemActivityId;
    //represend type of current problemActivity
    private int activityTypesId;
    //id of user who added this problemActivity
    private int userId;

    //contains text of current problemActivity
    private String content;
    //contains adding date of current problemActivity
    private String date;
    //name of user who added this problemActivity
    private String userName;

    /**
     * provide access to all fields of class
     */
    public int getCommentId() {
        return problemActivityId;
    }

    public int getActivityTypesId() {
        return activityTypesId;
    }

    public int getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

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
    public ProblemActivity(int problemActivityId, int activityTypesId, int userId, String content,
                           String date, String userName) {
        this.problemActivityId = problemActivityId;
        this.activityTypesId = activityTypesId;
        this.userId = userId;
        this.content = content;
        this.date = date;
        this.userName = userName;
    }
}