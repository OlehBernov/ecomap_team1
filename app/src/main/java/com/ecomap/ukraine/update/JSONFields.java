package com.ecomap.ukraine.update;

/**
 * Field names in JSON, which comes from the server.
 */
public class JSONFields {

    /**
     * Problem id.
     */
    public static final String ID = "Id";

    /**
     * Problems id.
     */
    public static final String PROBLEMS_ID = "Problems_Id";

    /**
     * Status of problem.
     */
    public static final String PROBLEM_STATUS = "Status";

    /**
     * Problem type id.
     */
    public static final String PROBLEM_TYPES_ID = "ProblemTypes_Id";

    /**
     * Title of problem.
     */
    public static final String TITLE = "Title";

    /**
     * Date of problem creation.
     */
    public static final String PROBLEM_DATE = "Date";

    /**
     * Latitude of problem localization.
     */
    public static final String LATITUDE = "Latitude";

    /**
     * Longitude of problem localization.
     */
    public static final String LONGITUDE = "Longtitude"; //must be with a mistake

    /**
     * Description of the problem.
     */
    public static final String PROBLEM_CONTENT = "Content";

    /**
     * Possible solutions of problems.
     */
    public static final String PROPOSAL = "Proposal";

    /**
     * Moderation status of problem.
     */
    public static final String MODERATION = "Moderation";

    /**
     * Severity of problem.
     */
    public static final String SEVERITY = "Severity";

    /**
     * Votes of problem.
     */
    public static final String VOTES = "Votes";

    /**
     * Problem activity type id.
     */
    public static final String ACTIVITY_TYPES_ID = "ActivityTypes_Id";

    /**
     * Problem activity creators id.
     */
    public static final String COMMENT_USERS_ID = "Users_Id";

    /**
     * Date of problem activity creation.
     */
    public static final String PROBLEM_ACTIVITY_DATE = "Date";

    /**
     * Type of problem activity.
     */
    public static final String PROBLEM_ACTIVITY_CONTENT = "Content";

    /**
     * Description of the problem activity.
     */
    public static final String COMMENT_CORE = "Content";

    /**
     * Problem activity creators name.
     */
    public static final String USER_NAME = "userName";

    /**
     * Id of user, which added a photo.
     */
    public static final String PHOTO_USERS_ID = "Users_Id";

    /**
     * Photo status.
     */
    public static final String PHOTO_STATUS = "Status";

    /**
     * Link to the photo.
     */
    public static final String LINK = "Link";

    /**
     * Photo description.
     */
    public static final String PHOTO_DESCRIPTION = "Description";

    /**
     * Position of problem details in JSON array.
     */
    public static final int DETAILS_POSITION = 0;

    /**
     * Position of problem photos in JSON array.
     */
    public static final int PHOTOS_POSITION = 1;

    /**
     * Position of problem activities in JSON array.
     */
    public static final int PROBLEM_ACTIVITY_POSITION = 2;

    /**
     * Fields for top_10
     */
    public static final String TOP_10_ITEM_ID = "Id";

    /**
     * Title of problem, whicg is in top10
     */
    public static final String TOP_10_TITLE = "Title";

    /**
     * Number of votes of problem, which is in top 10 by votes
     */
    public static final String TOP_10_VOTES = "Votes";

    /**
     * Severity of problem, which is in top 10 by severity
     */
    public static final String TOP_10_SEVERITY = "Severity";

    /**
     * Number of comments of problem, which is in top 10 by comments
     */
    public static final String TOP_10_COMMENTS = "value";

    /**
     * Id of problem, which for posting vote
     */
    public static final String PROBLEM_ID = "idProblem";

    /**
     * Id of user, who posted vote
     */
    public static final String USER_ID = "userId";

    /**
     * Id of user, who posted comment
     */
    public static final String USER_SURNAME = "userSurname";

    /**
     * Content of comment
     */
    public static final String CONTENT = "Content";

    /**
     * Data of comment
     */
    public static final String DATA = "data";

    /**
     * Id of problem type in statistics JSON.
     */
    public static final String STATISTICS_ID = "id";

    /**
     * Name of statistics value on JSON field.
     */
    public static final String STATISTICS_VALUE = "value";

}
