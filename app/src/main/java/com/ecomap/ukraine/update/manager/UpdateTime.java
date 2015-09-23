package com.ecomap.ukraine.update.manager;

/**
 * Possible modes of data updating.
 */
public enum UpdateTime {

    /**
     * Updating performs when application starts.
     */
    EVERY_TIME(0),

    /**
     * Updating performs once a day.
     */
    ONCE_A_DAY(1),

    /**
     * Updating performs once a week.
     */
    ONCE_A_WEEK(2),

    /**
     * Updating performs once a month.
     */
    ONCE_A_MONTH(3);

    /**
     * Time in milliseconds for updating each time when application starts.
     */
    private static final int UPDATE = 20_000;

    /**
     * Time in milliseconds for updating once a day.
     */
    private static final int DAY_IN_MILLISECONDS = 86_400_000;

    /**
     * Time in milliseconds for updating once a week.
     */
    private static final int WEEK_IN_MILLISECONDS = 604_800_000;

    /**
     * Time in milliseconds for updating once a month.
     */
    private static final long MONTH_IN_MILLISECONDS = 26_297_438_30L;

    private int id;

    /**
     * Constructor of updating type mode.
     *
     * @param id id of the updating time mode.
     */
    UpdateTime(int id) {
        this.id = id;
    }

    /**
     * Return type of updating time according to problem type id.
     *
     * @param id update time id.
     * @return update time type.
     */
    public static UpdateTime getUpdateTimeType(int id) {
        for (UpdateTime type : UpdateTime.values()) {
            if (type.id == id) {
                return type;
            }
        }
        return ONCE_A_WEEK;
    }

    /**
     * Returns id of the current problem.
     *
     * @return id of the problem.
     */
    public int getId() {
        return id;
    }

    /**
     * Transforms updating time mode id to time in milliseconds for this mode.
     *
     * @return time in milliseconds.
     */
    public long getTimeInMilliseconds() {
        switch (id) {
            case 0:
                return UPDATE;
            case 1:
                return DAY_IN_MILLISECONDS;
            case 2:
                return WEEK_IN_MILLISECONDS;
            case 3:
                return MONTH_IN_MILLISECONDS;
            default:
                return WEEK_IN_MILLISECONDS;
        }
    }

}