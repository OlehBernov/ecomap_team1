package com.ecomap.ukraine.data.manager;

import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.User;

import java.util.List;

/**
 * Interface of class, which receives server responses.
 */
public interface RequestReceiver {

    /**
     * Receives server response to the request of all problems,
     * and put it into database.
     *
     * @param problems list of all problems.
     */
    void setAllProblemsRequestResult(List<Problem> problems);

    /**
     * Receives server response to the request of details of
     * concrete problem, and put it into database.
     *
     * @param details details of concrete problem.
     */
    void setProblemDetailsRequestResult(Details details);

    /**
     * TODO docs
     *
     * @param user
     */
    void setLogInRequestResult(User user);

    /**
     * TODO
     *
     * @param success
     */
    void setLogOutRequestResult(boolean success);
}
