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
    private  User user;
    private final List<Bitmap> bitmaps;
    private final List<String> photoDescriptions;

    public NewProblemData (
            final String title,
            final String content,
            final String proposal,
            final String type,
            final LatLng position,
            final User user,
            final List<Bitmap> bitmaps,
            final List<String> photoDescriptions) {

        this.title = title;
        this.content = content;
        this.proposal = proposal;
        this.type = type;
        this.position = position;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public List<Bitmap> getPhotos() {
        return bitmaps;
    }

    public List<String> getPhotoDescriptions() {
        return photoDescriptions;
    }
}

