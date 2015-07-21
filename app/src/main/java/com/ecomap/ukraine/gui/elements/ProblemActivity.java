package com.ecomap.ukraine.gui.elements;

/**
 * Created by Andriy on 17.07.2015.
 */
public class ProblemActivity {
    private int problemActivityId;
    private int activityTypesId;
    private int userId;

    private String content;
    private String date;
    private String userName;
    
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