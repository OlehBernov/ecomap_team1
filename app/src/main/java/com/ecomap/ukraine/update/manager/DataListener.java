package com.ecomap.ukraine.update.manager;

import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import java.util.List;

/**
 * Interface of classes, which want to get
 * information about problem from database.
 */
public interface DataListener {

    /**
     * Send to listeners list of all problems.
     *
     * @param problems list of all problems.
     */
    void onAllProblemsUpdate(final List<Problem> problems);

    /**
     * Send to listeners object of problem details.
     *
     * @param details details of concrete problem.
     */
    void onProblemDetailsUpdate(final Details details);

    /**
     * Send to listeners oject, which contains top10 elements
     * @param allTop10Items object, which contains top 10 elements
     */
    void onTop10Update(final AllTop10Items allTop10Items);

    /**
     * Performs when vote successfully sent to server
     */
    void onVoteAdded();

    /**
     * Performs when comment was successfully sent to server.
     */
    void onCommentAdded();

}
