package com.ecomap.ukraine.problemupdate.manager;

import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import java.util.List;

/**
 * Interface of classes, which want to get
 * information about problem from database.
 */
public interface ProblemListener {

    /**
     * Send to listeners list of all problems.
     *
     * @param problems list of all problems.
     */
    void updateAllProblems(final List<Problem> problems);

    /**
     * Send to listeners list of problem details.
     *
     * @param details details of concrete problem.
     */
    void updateProblemDetails(final Details details);

}
