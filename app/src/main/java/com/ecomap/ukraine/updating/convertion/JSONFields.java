package com.ecomap.ukraine.updating.convertion;

/**
 * Field names in JSON, which comes from the server.
 *
 * @author Kyrychenko Oleksandr
 */
public class JSONFields {

    /**
     * Problem id.
     */
    public static final String ID = "Id";

    /**
     * Problems id.
     */
    public static final String PROBLEMS_ID = "Problems_Id" ;

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
     * Longtitude of problem localization.
     */
    public static final String LONGITUDE = "Longtitude";

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

    public static final String USER_ID = "id";

    public static final String NAME = "name";

    public static final String SURNAME = "surname";

    public static final String ROLE = "role";

    public static final String IAT= "iat";

    public static final String TOKEN = "token";

    public static final String EMAIL = "email";

    public static final String PASSWORD = "password";

    /**
     * Id of registration
     */
    public static final String REGISTRATION_ID = "id";

    /**
     * Id of user who is registered
     */
    public static final String ID_OF_REGISTRATION_USER = "insertId";

    /**
     * Description of registration
     */
    public static final String  FIRST_NAME= "first_name";

    public static final String LAST_NAME = "last_name";



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

}
