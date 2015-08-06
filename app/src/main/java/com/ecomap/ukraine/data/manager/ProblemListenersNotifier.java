package com.ecomap.ukraine.data.manager;

import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import java.util.List;

/**
 * Interface of class, which performs notification listeners
 * about readiness of information about problems.
 */
interface ProblemListenersNotifier {

    /**
     * Adds the specified listener to the set of problemListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the ProblemListener to add.
     */
    void registerProblemListener(final ProblemListener listener);

    /**
     * Removes the specified listener from the set of problemaListeners.
     *
     * @param listener the ProblemListener to remove.
     */
    void removeProblemListener(final ProblemListener listener);


    /**
     * Send to listeners list of all problems.
    */
    void sendAllProblems(final List<Problem> problems);

    /**
     * Send to listeners details of concrete problem.
     */
    void sendProblemDetails(final Details details);
}
