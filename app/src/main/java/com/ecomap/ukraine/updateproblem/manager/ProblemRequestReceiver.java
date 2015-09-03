package com.ecomap.ukraine.updateproblem.manager;

import com.ecomap.ukraine.model.Details;
import com.ecomap.ukraine.model.Problem;

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

}
