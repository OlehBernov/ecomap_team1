package com.ecomap.ukraine.gui.elements;

/**
 * Created by Andriy on 17.07.2015.
 */
public class Photo {

    private int photoId;
    private int userId;
    private int status;

    private String link;
    private String description;

    public int getPhotoId() {
        return photoId;
    }

    public int getUserId() {
        return userId;
    }

    public int getStatus() {
        return status;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public Photo(int photoId, int userId, int status, String link, String description) {
        this.photoId = photoId;
        this.userId = userId;
        this.status = status;
        this.link = link;
        this.description = description;
    }

}
