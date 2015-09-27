package com.ecomap.ukraine.update.manager;

import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.Statistics;

import java.util.List;


public class DataListenerAdapter implements DataListener {

    /**
     * Receive list of all problems.
     *
     * @param problems list of all problems.
     */
    @Override
    public void onAllProblemsUpdate(List<Problem> problems) {

    }

    /**
     * Receive object of problem details.
     *
     * @param details object of problem details.
     */
    @Override
    public void onProblemDetailsUpdate(Details details) {

    }

    /**
     * Sends to listeners object of statistics of problem posting.
     *
     * @param statistics statistics of problem posting.
     */
    @Override
    public void onStatisticsUpdate(Statistics statistics) {

    }

    /**
     * Receive object, which contains top10 elements
     *
     * @param allTop10Items object, which contains top 10 elements
     */
    @Override
    public void onTop10Update(AllTop10Items allTop10Items) {

    }

    /**
     * Performs when vote successfully sent to server
     */
    @Override
    public void onVoteAdded() {

    }

    /**
     * Performs when comment was successfully sent to server.
     */
    @Override
    public void onCommentAdded() {

    }

}