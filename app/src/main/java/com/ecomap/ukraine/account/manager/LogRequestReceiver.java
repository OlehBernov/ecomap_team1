package com.ecomap.ukraine.account.manager;

import com.ecomap.ukraine.models.User;

/**
 * Interface of class, which receives server responses.
 */
public interface LogRequestReceiver {

    /**
     * Receives server response to the request identification
     *
     * @param user registered user
     */
    void setLogInRequestResult(User user);

    /**
     * Puts user information to Shared Preferences
     *
     * @param user user information
     * @param password user password
     */
    void putUserToPreferences (User user, String password);

}
