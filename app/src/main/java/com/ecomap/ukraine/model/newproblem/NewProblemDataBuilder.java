package com.ecomap.ukraine.model.newproblem;

import android.graphics.Bitmap;

import com.ecomap.ukraine.model.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Builder of NewProblemData
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

    /**
     * Constructor
     */
    public NewProblemDataBuilder () {

    }

    /**
     * Sets title field of builder
     * @param title
     */
    public NewProblemDataBuilder setTitle (String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets content field of builder
     * @param content
     */
    public NewProblemDataBuilder setContent (String content) {
        this.content = content;
        return this;
    }

    /**
     * Sets proposal field of builder
     * @param proposal
     */
    public NewProblemDataBuilder setProposal (String proposal) {
        this.proposal = proposal;
        return this;
    }

    /**
     * Sets type field of builder
     * @param type
     */
    public NewProblemDataBuilder setType (String type) {
        this.type = type;
        return this;
    }

    /**
     * Sets position field of builder
     * @param position
     */
    public NewProblemDataBuilder setPosition (LatLng position) {
        this.position = position;
        return this;
    }

    /**
     * Sets user field of builder
     * @param user
     */
    public NewProblemDataBuilder setUser (User user) {
        this.user = user;
        return this;
    }

    /**
     * Sets bitmaps field of builder
     * @param bitmaps
     */
    public NewProblemDataBuilder setPhotos (List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        return this;
    }

    /**
     * Sets photoDescriptions field of builder
     * @param photoDescriptions
     */
    public NewProblemDataBuilder setPhotoDescriptions (List<String> photoDescriptions) {
        this.photoDescriptions = photoDescriptions;
        return this;
    }

    /**
     * Build new NewProblemData instance fom builder
     */
    public NewProblemData build () {
        return new NewProblemData(title, content, proposal, type,
                position, user, bitmaps, photoDescriptions);
    }

    /**
     * Sets default data on builder
     */
    public NewProblemDataBuilder setdefaultData () {
        this.title = "";
        this.content = "";
        this.proposal = "";
        this.type = "";
        this.position =  new LatLng(0f, 0f);
        this.user = User.ANONYM_USER;
        this.bitmaps = null;
        this.photoDescriptions = null;
        return  this;
    }


}
