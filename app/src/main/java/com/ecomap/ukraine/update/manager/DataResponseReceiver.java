package com.ecomap.ukraine.update.manager;

import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import java.util.List;

/**
 * Interface of class, which receives server responses.
 */
public interface DataResponseReceiver {

    /**
     * Receives server response to the request of all problems,
     * and put it into database.
     *
     * @param problems list of all problems.
     */
    void getAllProblemsResponseResult(final List<Problem> problems);

    /**
     * Receives server response to the request of details of
     * concrete problem, and put it into database.
     *
     * @param details details of concrete problem.
     */
    void getProblemDetailsResponseResult(final Details details);

    /**
     * Receive server response to the request of top 10
     * problems and send int to listeners
     * @param allTop10Items object of top 10 problem
     */
    void getTop10ResponseResult (final AllTop10Items allTop10Items);

    /**
     * Performs when comment was successfully sent to server.
     */
    void onVoteAdded();

    /**
     * Performs when comment was successfully sent to server.
     */
    void onCommentAdded();

}
