package com.ecomap.ukraine.gui.elements;

import java.util.List;

/**
 * Created by Andriy on 17.07.2015.
 */
public class Details {

    public static final int CREATOR_ACTIVITY = 0;

    int severity;
    int moderations;
    int votes;

    String decription;
    String proposal;

    List<Comment> comments;
    List <Photo> photos;

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

    public String getFirstName() {
        return comments.get(CREATOR_ACTIVITY).getFirstName();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Details(int severity, int moderations, int votes, String decription,
                   String proposal, List<Comment> comments, List<Photo> photos) {
        this.severity = severity;
        this.moderations = moderations;
        this.votes = votes;
        this.decription = decription;
        this.proposal = proposal;
        this.comments = comments;
        this.photos = photos;
    }
}
