package com.ecomap.ukraine.gui.elements;

/**
 * Created by Andriy on 17.07.2015.
 */
public class Comment {
    int commentId;
    int activityTypesId;
    int userId;

    String content;
    String date;

    public int getCommentId() {
        return commentId;
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
}
