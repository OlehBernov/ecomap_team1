package com.ecomap.ukraine.models.new_problem_data;

import android.graphics.Bitmap;

import com.ecomap.ukraine.models.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Andriy on 31.08.2015.
 */
public class NewProblemData {
    private final String title;
    private final String content;
    private final String proposal;
    private final String type;
    private final LatLng position;
    private final String userId;
    private final String userName;
    private final String userSurname;
    private final List<Bitmap> bitmaps;
    private final List<String> photoDescriptions;

    public NewProblemData (
            final String title,
            final String content,
            final String proposal,
            final String type,
            final LatLng position,
            final String userId,
            final String userName,
            final String userSurname,
            final List<Bitmap> bitmaps,
            final List<String> photoDescriptions) {

        this.title = title;
        this.content = content;
        this.proposal = proposal;
        this.type = type;
        this.position = position;
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.bitmaps = bitmaps;
        this.photoDescriptions = photoDescriptions;
    }

    public String getTitle () {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getProposal() {
        return proposal;
    }

    public String getType() {
        return type;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public List<Bitmap> getPhotos() {
        return bitmaps;
    }

    public List<String> getPhotoDescriptions() {
        return photoDescriptions;
    }
}
