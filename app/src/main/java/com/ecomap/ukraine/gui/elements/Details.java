package com.ecomap.ukraine.gui.elements;

import java.util.ArrayList;

/**
 * Created by Andriy on 17.07.2015.
 */
public class Details {

    int severity;
    int moderations;
    int votes;

    String decription;
    String proposal;

    ArrayList <Comment> comments;
    ArrayList <Photo> photos;

    public int getSeverity() {
        return severity;
    }

    public int getModerations() {
        return moderations;
    }

    public int getVotes() {
        return votes;
    }

    public String getDecription() {
        return decription;
    }

    public String getProposal() {
        return proposal;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
