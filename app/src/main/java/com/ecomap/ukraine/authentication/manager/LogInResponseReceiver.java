package com.ecomap.ukraine.authentication.manager;

import com.ecomap.ukraine.models.User;

/**
 * Interface of class, which receives server responses.
 */
public interface LogInResponseReceiver {

    /**
     * Receives server response to the request identification
     *
     * @param user registered user
     */
    void getLogInResponseResult(User user);

}
