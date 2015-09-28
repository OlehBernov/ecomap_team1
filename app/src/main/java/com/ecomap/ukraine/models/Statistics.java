package com.ecomap.ukraine.models;


import android.util.SparseIntArray;

import java.util.Map;

/**
 * Represents statistics adding problems for a certain period.
 */
public class Statistics {

    /**
     * Key and part of api address for day period.
     */
    public static final String DAILY = "D";

    /**
     * Key and part of api address for week period.
     */
    public static final String WEEKLY = "W";

    /**
     * Key and part of api address for month period.
     */
    public static final String MONTH = "M";

    /**
     * Key and part of api address for year period.
     */
    public static final String ANNUAL = "Y";

    /**
     * Key and part of api address for all time period.
     */
    public static final String FOR_ALL_TIME = "A";

    /**
     * Contains values for relevant period.
     */
    private Map<String, SparseIntArray> statisticsItems;

    public Statistics(Map<String, SparseIntArray> statisticsItems) {
        this.statisticsItems = statisticsItems;
    }

    public int size() {
        return (statisticsItems.size() == 0)
                ? 0
                : statisticsItems.get(FOR_ALL_TIME).size();
    }

    public SparseIntArray getDailyStatistics() {
        return statisticsItems.get(DAILY);
    }

    public SparseIntArray getWeeklyStatistics() {
        return statisticsItems.get(WEEKLY);
    }

    public SparseIntArray getMonthStatistics() {
        return statisticsItems.get(MONTH);
    }

    public SparseIntArray getAnnualStatistics() {
        return statisticsItems.get(ANNUAL);
    }

    public SparseIntArray getStatisticsForAllTime() {
        return statisticsItems.get(FOR_ALL_TIME);
    }

}
