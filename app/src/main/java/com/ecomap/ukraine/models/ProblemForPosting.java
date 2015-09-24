package com.ecomap.ukraine.models;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Data of new created problem
 */
public class ProblemForPosting {

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
     * Photos of problem
     */
    private final List<Bitmap> bitmaps;

    /**
     * Descriptions of problem photos
     */
    private final List<String> photoDescriptions;

    /**
     * Author of problem
     */
    private User user;

    /**
     * Constructor
     *
     * @param title             Title of problem
     * @param content           Description of problem
     * @param proposal          Proposition of solving problem
     * @param type              Type of problem
     * @param position          Problem position
     * @param user              Author of problem
     * @param bitmaps           Photos of problem
     * @param photoDescriptions Descriptions of problem photos
     */
    public ProblemForPosting(
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
    public String getTitle() {
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

    /**
     * Builder of ProblemForPosting
     */
    public static class Builder {
        private String title;
        private String content;
        private String proposal;
        private String type;
        private LatLng position;
        private User user;
        private List<Bitmap> bitmaps;
        private List<String> photoDescriptions;

        /**
         * Constructor
         */
        public Builder() {

        }

        /**
         * Sets title field of builder
         *
         * @param title
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets content field of builder
         *
         * @param content
         */
        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        /**
         * Sets proposal field of builder
         *
         * @param proposal
         */
        public Builder setProposal(String proposal) {
            this.proposal = proposal;
            return this;
        }

        /**
         * Sets type field of builder
         *
         * @param type
         */
        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        /**
         * Sets position field of builder
         *
         * @param position
         */
        public Builder setPosition(LatLng position) {
            this.position = position;
            return this;
        }

        /**
         * Sets user field of builder
         *
         * @param user
         */
        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        /**
         * Sets bitmaps field of builder
         *
         * @param bitmaps
         */
        public Builder setPhotos(List<Bitmap> bitmaps) {
            this.bitmaps = bitmaps;
            return this;
        }

        /**
         * Sets photoDescriptions field of builder
         *
         * @param photoDescriptions
         */
        public Builder setPhotoDescriptions(List<String> photoDescriptions) {
            this.photoDescriptions = photoDescriptions;
            return this;
        }

        /**
         * Build new ProblemForPosting instance fom builder
         */
        public ProblemForPosting build() {
            return new ProblemForPosting(title, content, proposal, type,
                    position, user, bitmaps, photoDescriptions);
        }

        /**
         * Sets default data on builder
         */
        public Builder setdefaultData() {
            title = "";
            content = "";
            proposal = "";
            type = "";
            position = new LatLng(0f, 0f);
            user = User.ANONYM_USER;
            bitmaps = null;
            photoDescriptions = null;
            return this;
        }

    }
}

