package com.ecomap.ukraine.updateproblem.manager;

import com.ecomap.ukraine.model.Details;
import com.ecomap.ukraine.model.Problem;

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
