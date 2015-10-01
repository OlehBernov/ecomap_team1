package com.ecomap.ukraine.update;


import android.util.SparseIntArray;

import com.ecomap.ukraine.models.Statistics;
import com.ecomap.ukraine.update.manager.DataResponseReceiver;

import java.util.HashMap;
import java.util.Map;


/**
 * Implements Statistics object creation after processing all statistics request.
 */
public class StatisticsLoader implements StatisticsResponseReceiver {

    private static final int STATISTIC_ITEMS_COUNT = 5;

    private DataResponseReceiver dataResponseReceiver;
    private Map<String, SparseIntArray> statisticsItems;

    /**
     * Builds class and initializes listeners.
     *
     * @param dataResponseReceiver receiver of the complex result of all statistics requests.
     */
    public StatisticsLoader(final DataResponseReceiver dataResponseReceiver) {
        this.dataResponseReceiver = dataResponseReceiver;
        statisticsItems = new HashMap<>();
    }

    /**
     * Starts statistics loading process.
     *
     * @param loadingClient client which have relation to the server.
     */
    @Override
    public void loadStatistics(final LoadingClient loadingClient) {
        loadingClient.getStatisticsItem(Statistics.DAILY, this);
        loadingClient.getStatisticsItem(Statistics.WEEKLY, this);
        loadingClient.getStatisticsItem(Statistics.MONTH, this);
        loadingClient.getStatisticsItem(Statistics.ANNUAL, this);
        loadingClient.getStatisticsItem(Statistics.FOR_ALL_TIME, this);
    }

    /**
     * Adds response with statistics information about problem posting for certain period to
     * resulting map.
     *
     * @param period         certain period.
     * @param statisticsItem server response.
     */
    @Override
    public synchronized void onStatisticItemResponse(final String period,
                                                     final SparseIntArray statisticsItem) {
        statisticsItems.put(period, statisticsItem);
        if (isStatisticsObjectReady(statisticsItems)) {
            dataResponseReceiver.setStatisticsResponse(new Statistics(statisticsItems));
        }
    }

    /**
     * Checks if all information about statistics is ready.
     *
     * @param statisticsItems all downloaded period for statistics.
     * @return is information ready.
     */
    private synchronized boolean isStatisticsObjectReady(final Map<String, SparseIntArray> statisticsItems) {
        return statisticsItems.size() == STATISTIC_ITEMS_COUNT;
    }

}
