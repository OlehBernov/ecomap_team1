package com.ecomap.ukraine.model;

/**
 * Possible types of problem activity.
 */
public enum ActivityType {

    /**
     * Creation activity.
     */
    CREATE(1),
    //TODO delete unused types
    UNKNOWN(2),

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

    UNKNOWN2(6),

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
