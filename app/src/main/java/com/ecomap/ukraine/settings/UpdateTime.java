package com.ecomap.ukraine.settings;


public enum UpdateTime {
    EVERY_TIME (0),
    ONCE_A_DAY (1),
    ONCE_A_WEEK (2),
    ONCE_A_MONTH (3);

    private static final int UPDATE = 20_000;
    private static final int DAY_IN_MILLISECONDS = 86_400_000;
    private static final int WEEK_IN_MILLISECONDS = 604_800_000;
    private static final long MONTH_IN_MILLISECONDS = 26_297_438_30L;

    private int id;

    UpdateTime(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

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
