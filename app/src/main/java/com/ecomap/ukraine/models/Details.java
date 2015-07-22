package com.ecomap.ukraine.models;

import java.util.List;

/**
 * Class Details represent detail information about problem.
 * Created by Andriy on 17.07.2015.
 */
public class Details {

    /**
     * represents the level of severity of the problem (from 1 to 5)
     */
    private int severity;
    /**
     * number of changes to this problem
     */
    private int moderations;
    /**
     * number of people, who set like to this problem
     */
    private int votes;

    /**
     * contains description of current problem
     */
    private String description;
    /**
     * contains proposals for solving this problem
     */
    private String proposal;

    /**
     * array of problemActivity that are related to the problem
     */
    private List<ProblemActivity> problemActivities;
    /**
     *  array of photos that are related to the problem
     */
    private List <Photo> photos;

    /**
     * provides access to severity
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * provides access to moderations
     */
    public int getModerations() {
        return moderations;
    }

    /**
     * provides access to votes
     */
    public int getVotes() {
        return votes;
    }

    /**
     * provides access to description
     */
    public String getDescription() {
        return description;
    }

    /**
     * provides access to proposal
     */
    public String getProposal() {
        return proposal;
    }

    /**
     * provides access to problemActivities
     */
    public List<ProblemActivity> getProblemActivities() {
        return problemActivities;
    }

    /**
     * provides access to photos
     */
    public List<Photo> getPhotos() {
        return photos;
    }


    /**
     * Constructor of class
     * @param severity represents the level of severity of the problem (from 1 to 5)
     * @param moderations number of changes to this problem
     * @param votes number of people, who set like to this problem
     * @param description contains description of current problem
     * @param proposal contains proposals for solving this problem
     * @param problemActivities array of problemActivity that are related to the problem
     * @param photos array of photos that are related to the problem
     */
    public Details(int severity, int moderations, int votes, String description, String proposal,
                   List<ProblemActivity> problemActivities, List<Photo> photos) {
        this.severity = severity;
        this.moderations = moderations;
        this.votes = votes;
        this.description = description;
        this.proposal = proposal;
        this.problemActivities = problemActivities;
        this.photos = photos;
    }
}
