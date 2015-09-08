package com.ecomap.ukraine.models;

/**
 * Class ProblemActivity represent information about activity that are related to the problem.
 * Activity - some action which is related to the problem (like, comment, creation).
 * Created by Andriy on 17.07.2015.
 */
public class ProblemActivity {

    /**
     * id of the problem
     */
    private int problemId;

    /**
     * id of current problemActivity
     */
    private int problemActivityId;

    /**
     * Represent type of current problemActivity
     */
    private ActivityType activityType;

    /**
     * id of user who added this problemActivity
     */
    private int userId;

    /**
     * contains text of current problemActivity
     */
    private String content;

    /**
     * contains adding date of current problemActivity
     */
    private String date;

    /**
     * name of user who added this problemActivity
     */
    private String userName;

    /**
     * Constructor of class
     *
     * @param problemActivityId id of current problemActivity
     * @param activityType      represend type of current problemActivity
     * @param userId            id of user who added this problemActivity
     * @param content           contains text of current problemActivity
     * @param date              contains adding date of current problemActivity
     * @param userName          name of user who added this problemActivity
     */
    public ProblemActivity(int problemId, int problemActivityId, ActivityType activityType, int userId,
                           String content, String date, String userName) {
        this.problemId = problemId;
        this.problemActivityId = problemActivityId;
        this.activityType = activityType;
        this.userId = userId;
        this.content = content;
        this.date = date;
        this.userName = userName;
    }

    /**
     * returns problem id
     *
     * @return problem id
     */
    public int getProblemId() {
        return problemId;
    }

    /**
     * provides access to position
     */
    public int getProblemActivityId() {
        return problemActivityId;
    }

    /**
     * provides access to position
     */
    public ActivityType getActivityType() {
        return activityType;
    }

    /**
     * provides access to userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * provides access to content
     */
    public String getContent() {
        return content;
    }

    /**
     * provides access to date
     */
    public String getDate() {
        return date;
    }

    /**
     * provides access to userName
     */
    public String getFirstName() {
        return userName;
    }

    /**
     * Possible types of problem activity.
     */
    public enum ActivityType {

        /**
         * Creation activity.
         */
        CREATE(1),

        /**
         * User like.
         */
        LIKE(3),

        /**
         * Photo adding.
         */
        PHOTO(4),

        /**
         * Problem comment.
         */
        COMMENT(5),

        /**
         * Default type for unknown activity.
         */
        UNKNOWN_TYPE(7);

        private int id;

        /**
         * Constructor of activity type.
         *
         * @param id id of the activity type.
         */
        ActivityType(int id) {
            this.id = id;
        }

        /**
         * Returns activity type according to activity type id.
         *
         * @param id activity type id.
         * @return activity type.
         */
        public static ActivityType getActivityType(int id) {
            for (ActivityType type : ActivityType.values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return UNKNOWN_TYPE;
        }

        /**
         * Return the id of activity type.
         *
         * @return activity type id.
         */
        public int getId() {
            return id;
        }

    }
}