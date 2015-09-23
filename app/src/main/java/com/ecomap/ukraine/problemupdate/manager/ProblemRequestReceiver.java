package com.ecomap.ukraine.problemupdate.manager;

import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import java.util.List;

/**
 * Interface of class, which receives server responses.
 */
public interface ProblemRequestReceiver {

    /**
     * Receives server response to the request of all problems,
     * and put it into database.
     *
     * @param problems list of all problems.
     */
    void setAllProblemsRequestResult(final List<Problem> problems);

    /**
     * Receives server response to the request of details of
     * concrete problem, and put it into database.
     *
     * @param details details of concrete problem.
     */
    void setProblemDetailsRequestResult(final Details details);

    void setTop10RequestResult (final AllTop10Items allTop10Items);

    void onVoteAdded();

    void onCommentAdded();

}
