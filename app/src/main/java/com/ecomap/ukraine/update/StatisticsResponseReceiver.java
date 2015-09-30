package com.ecomap.ukraine.update;


import android.util.SparseIntArray;

/**
 * Interface for statistics loader.
 */
public interface StatisticsResponseReceiver {

    /**
     * Starts statistics loading process.
     *
     * @param loadingClient client which have relation to the server.
     */
    void loadStatistics(LoadingClient loadingClient);

    /**
     * Sends result of the one request to statistics loader.
     *
     * @param period identifier of the statistics type request.
     * @param statisticsItem server response.
     */
    void onStatisticItemResponse(String period, SparseIntArray statisticsItem);

}
