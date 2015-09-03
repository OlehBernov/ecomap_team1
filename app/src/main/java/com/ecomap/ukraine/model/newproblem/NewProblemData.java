package com.ecomap.ukraine.model.newproblem;

import android.graphics.Bitmap;

import com.ecomap.ukraine.model.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Data of new created problem
 */
public class NewProblemData {
    /**
     * Title of problem
     */
    private final String title;
    /**
     * Description of problem
     */
    private final String content;
    /**
     * Proposition of solving problem
     */
    private final String proposal;
    /**
     * Type of problem
     */
    private final String type;
    /**
     * Problem position
     */
    private final LatLng position;
    /**
     * Author of problem
     */
    private  User user;
    /**
     * Photos of problem
     */
    private final List<Bitmap> bitmaps;
    /**
     * Descriptions of problem photos
     */
    private final List<String> photoDescriptions;

    /**
     * Constructor
     * @param title Title of problem
     * @param content Description of problem
     * @param proposal Proposition of solving problem
     * @param type Type of problem
     * @param position Problem position
     * @param user Author of problem
     * @param bitmaps Photos of problem
     * @param photoDescriptions Descriptions of problem photos
     */
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

    /**
     * Provide access to title field
     */
    public String getTitle () {
        return title;
    }

    /**
     * Provide access to content field
     */
    public String getContent() {
        return content;
    }

    /**
     * Provide access to proposal field
     */
    public String getProposal() {
        return proposal;
    }

    /**
     * Provide access to position field
     */
    public String getType() {
        return type;
    }

    /**
     * Provide access to user field
     */
    public LatLng getPosition() {
        return position;
    }

    /**
     * Provide access to title field
     */
    public User getUser() {
        return user;
    }

    /**
     * Provide access to bitmaps field
     */
    public List<Bitmap> getPhotos() {
        return bitmaps;
    }

    /**
     * Provide access to photoDescriptions field
     */
    public List<String> getPhotoDescriptions() {
        return photoDescriptions;
    }
}

