package com.ecomap.ukraine.data.manager;

/**
 * Interface of class, which performs notification listeners
 * about readiness of information about problem.
 */
public interface ProblemListenersNotifier {

    /**
     * Adds the specified listener to the set of problemListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the ProblemListener to add.
     */
    void registerProblemListener(ProblemListener listener);

    /**
     * Removes the specified listener from the set of problemaListeners.
     *
     * @param listener the ProblemListener to remove.
     */
    void removeProblemListener(ProblemListener listener);

    /**
     * Notify all ProblemListeners about data manger response
     * and send them received information.
     *
     * @param requestType request type.
     * @param problem result problem.
     */
    void notifyProblemListeners(int requestType, Object problem);
}
