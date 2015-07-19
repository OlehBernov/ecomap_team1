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
    String firstName;
    
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
    
    public String getFirstName() {
        return firstName;
    }

    public Comment(int commentId, int activityTypesId, int userId, String content,
                   String date, String firstName) {
        this.commentId = commentId;
        this.activityTypesId = activityTypesId;
        this.userId = userId;
        this.content = content;
        this.date = date;
        this.firstName = firstName;
    }
}