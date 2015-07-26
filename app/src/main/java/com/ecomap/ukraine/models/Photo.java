package com.ecomap.ukraine.models;

import android.database.Cursor;

import com.ecomap.ukraine.database.DBContract;

/**
 * Class Photo represent information about photo of problem.
 * Created by Andriy on 17.07.2015.
 */
public class Photo {

    /**
     * id of the problem
     */

    private int problemId;

    /**
     * id of photo
     */
    private int photoId;

    /**
     * id of user who added this photo
     */
    private int userId;

    /**
     * status of this photo
     */
    private int status;

    /**
     * link to this photo
     */
    private String link;

    /**
     * description of this photo
     */
    private String description;

    public int getProblemId() {
        return problemId;
    }

    /**
     * provides access to photoId
     */
    public int getPhotoId() {
        return photoId;
    }
    /**
     * provides access to photoId
     */
    public int getUserId() {
        return photoId;
    }
    /**
     * provides access to status
     */
    public int getStatus() {
        return status;
    }
    /**
     * provides access to link
     */
    public String getLink() {
        return link;
    }
    /**
     * provides access to description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Constructor of class
     * @param photoId id of current problem
     * @param userId id of user who added this photo
     * @param status status of this photo
     * @param link link to this photo
     * @param description description of this photo
     */
    public Photo(int problemId, int photoId, int userId, int status, String link,
                 String description) {
        this.problemId = problemId;
        this.photoId = photoId;
        this.userId = userId;
        this.status = status;
        this.link = link;
        this.description = description;
    }

    public Photo(Cursor cursor, int problemId) {
        this.problemId = problemId;
        this.photoId = cursor.getInt(cursor.getColumnIndex(DBContract.Photos.PHOTO_ID));
        this.userId = cursor.getInt(cursor.getColumnIndex(DBContract.Photos.PHOTO_USERS_ID));
        this.status = cursor.getInt(cursor.getColumnIndex(DBContract.Photos.PHOTO_STATUS));
        this.link = cursor.getString(cursor.getColumnIndex(DBContract.Photos.LINK));
        this.description = cursor.getString(cursor.getColumnIndex(DBContract.Photos.PHOTO_DESCRIPTION));
    }

}
