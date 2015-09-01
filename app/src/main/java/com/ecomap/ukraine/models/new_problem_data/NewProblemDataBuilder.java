package com.ecomap.ukraine.models.new_problem_data;

import android.graphics.Bitmap;

import com.ecomap.ukraine.models.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Andriy on 31.08.2015.
 */
public class NewProblemDataBuilder {
    private  String title;
    private  String content;
    private  String proposal;
    private  String type;
    private  LatLng position;
    private  User user;
    private  List<Bitmap> bitmaps;
    private  List<String> photoDescriptions;

    public NewProblemDataBuilder () {

    }

    public NewProblemDataBuilder setTitle (String title) {
        this.title = title;
        return this;
    }

    public NewProblemDataBuilder setContent (String content) {
        this.content = content;
        return this;
    }

    public NewProblemDataBuilder setProposal (String proposal) {
        this.proposal = proposal;
        return this;
    }

    public NewProblemDataBuilder setType (String type) {
        this.type = type;
        return this;
    }

    public NewProblemDataBuilder setPosition (LatLng position) {
        this.position = position;
        return this;
    }

    public NewProblemDataBuilder setUser (User user) {
        this.user = user;
        return this;
    }

    public NewProblemDataBuilder setPhotos (List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        return this;
    }

    public NewProblemDataBuilder setPhotoDescriptions (List<String> photoDescriptions) {
        this.photoDescriptions = photoDescriptions;
        return this;
    }

    public NewProblemData build () {
        return new NewProblemData(title, content, proposal, type,
                position, user, bitmaps, photoDescriptions);
    }


}
