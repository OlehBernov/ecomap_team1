package com.ecomap.ukraine.update.manager;

import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import java.util.List;

/**
 * Created by Andriy on 22.09.2015.
 */
public class DataListenerAdapter implements DataListener {

    /**
     * Receive list of all problems.
     * @param problems list of all problems.
     */
    @Override
    public void updateAllProblems(List<Problem> problems) {

    }

    /**
     * Receive object of problem details.
     * @param details object of problem details.
     */
    @Override
    public void updateProblemDetails(Details details) {

    }
    /**
     * Receive oject, which contains top10 elements
     * @param allTop10Items object, which contains top 10 elements
     */
    @Override
    public void updateTop10(AllTop10Items allTop10Items) {

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