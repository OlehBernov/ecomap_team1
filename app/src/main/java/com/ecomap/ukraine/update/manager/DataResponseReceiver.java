package com.ecomap.ukraine.update.manager;

import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.Statistics;

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
    void setAllProblemsResponse(List<Problem> problems);

    /**
     * Receives server response to the request of details of
     * concrete problem, and put it into database.
     *
     * @param details details of concrete problem.
     */
    void setProblemDetailsResponse(Details details);

    /**
     * Receive server response to the request of top 10
     * problems and send it to listeners.
     *
     * @param allTop10Items object of top 10 problem
     */
    void setTop10Response(AllTop10Items allTop10Items);

    /**
     * Receive server response to the request of statistics about
     * problem posting and send it to listeners.
     *
     * @param statistics object of statistics.
     */
    void setStatisticsResponse(Statistics statistics);

    /**
     * Performs when comment was successfully sent to server.
     */
    void onVoteAdded();

    /**
     * Performs when comment was successfully sent to server.
     */
    void onCommentAdded();

}
