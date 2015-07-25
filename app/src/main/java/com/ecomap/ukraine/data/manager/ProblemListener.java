package com.ecomap.ukraine.data.manager;

import com.ecomap.ukraine.models.Problem;

import java.util.List;

/**
 * Interface ProblemListener must implements classes,
 * which want to get information about problem from database.
 */
public interface ProblemListener {

    /**
     * This method is called if the specified DataManager object's
     * notifyObservers method is called.
     *
     * @param requestType the type of request handled.
     * @param problem the result of request.
     */
    void update(int requestType, Object problem);
}
