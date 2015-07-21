package com.ecomap.ukraine.gui.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andriy on 17.07.2015.
 */
public class Details {


    private int severity;
    private int moderations;
    private int votes;

    private String decription;
    private String proposal;

    private List<ProblemActivity> problemActivities;
    private List <Photo> photos;

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

    public List<ProblemActivity> getProblemActivities() {
        return problemActivities;
    }

    public List<Photo> getPhotos() {
        return photos;
    }



    public Details(int severity, int moderations, int votes, String decription, String proposal,
                   List<ProblemActivity> problemActivities, List<Photo> photos) {
        this.severity = severity;
        this.moderations = moderations;
        this.votes = votes;
        this.decription = decription;
        this.proposal = proposal;
        this.problemActivities = problemActivities;
        this.photos = photos;
    }
}
