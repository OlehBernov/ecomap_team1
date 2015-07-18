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
    String lastName;

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

    public String getLastName() {
        return lastName;
    }

    public Comment(int commentId, int activityTypesId, int userId, String content,
                   String date, String firstName, String lastName) {
        this.commentId = commentId;
        this.activityTypesId = activityTypesId;
        this.userId = userId;
        this.content = content;
        this.date = date;
		this.firstName = firstName;
        this.lastName = lastName;
    }
}
