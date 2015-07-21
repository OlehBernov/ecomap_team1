package com.ecomap.ukraine.models;

import java.util.List;

/**
 * Class Details represent detail information about problem.
 * Created by Andriy on 17.07.2015.
 */
public class Details {


    //represents the level of severity of the problem (from 1 to 5)
    private int severity;
    //number of changes to this problem
    private int moderations;
    //number of people, who set like to this problem
    private int votes;

    //contains description of current problem
    private String decription;
    //contains proposals for solving this problem
    private String proposal;

    //array of problemActivity that are related to the problem
    private List<ProblemActivity> problemActivities;
    //array of photos that are related to the problem
    private List <Photo> photos;

    /**
     * provide access to all fields of class
     */
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


    /**
     * Constructor of class
     * @param severity represents the level of severity of the problem (from 1 to 5)
     * @param moderations number of changes to this problem
     * @param votes number of people, who set like to this problem
     * @param decription contains description of current problem
     * @param proposal contains proposals for solving this problem
     * @param problemActivities array of problemActivity that are related to the problem
     * @param photos array of photos that are related to the problem
     */
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
