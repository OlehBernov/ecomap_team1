package com.ecomap.ukraine.models;


import java.util.List;

/**
 * Class Details represent detail information about problem.
 * Created by Andriy on 17.07.2015.
 */
public class Details {

    /**
     * map of photos that are related to the problem
     */
    public List<Photo> photos;
    /**
     * Id of the problem.
     */
    private int problemId;
    /**
     * represents the level of severity of the problem (from 1 to 5)
     */
    private int severity;
    /**
     * number of changes to this problem
     */
    private int moderation;
    /**
     * number of people, who set like to this problem
     */
    private int votes;
    /**
     * contains content of current problem
     */
    private String content;
    /**
     * contains proposals for solving this problem
     */
    private String proposal;
    /**
     * the name giving to the particular problem.
     */
    private String title;
    /**
     * array of problemActivity that are related to the problem
     */
    private List<ProblemActivity> problemActivities;
    /**
     * date of the last data loading from server
     */
    private String lastUpdate;

    /**
     * Constructor of class
     *
     * @param severity          represents the level of severity of the problem (from 1 to 5)
     * @param moderation        number of changes to this problem
     * @param votes             number of people, who set like to this problem
     * @param content           contains content of current problem
     * @param proposal          contains proposals for solving this problem
     * @param problemActivities array of problemActivity that are related to the problem
     * @param photos            array of photos that are related to the problem
     */
    public Details(int problemId, int severity, int moderation, int votes, String content,
                   String proposal, String title, List<ProblemActivity> problemActivities,
                   List<Photo> photos, String lastUpdate) {
        this.problemId = problemId;
        this.severity = severity;
        this.moderation = moderation;
        this.votes = votes;
        this.content = content;
        this.proposal = proposal;
        this.title = title;
        this.problemActivities = problemActivities;
        this.photos = photos;
        this.lastUpdate = lastUpdate;
    }

    /**
     * returns id of the problem
     *
     * @return id of the problem
     */
    public int getProblemId() {
        return problemId;
    }

    /**
     * provides access to severity
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * provides access to moderation
     */
    public int getModeration() {
        return moderation;
    }

    /**
     * provides access to votes
     */
    public int getVotes() {
        return votes;
    }

    /**
     * provides access to content
     */
    public String getContent() {
        return content;
    }

    /**
     * provides access to proposal
     */
    public String getProposal() {
        return proposal;
    }

    /**
     * returns title of the problem
     *
     * @return title
     */
    public String getTitle() {
        return title;
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
     * returns date of the last data loading from server
     *
     * @return date of last onAllProblemsUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

}
