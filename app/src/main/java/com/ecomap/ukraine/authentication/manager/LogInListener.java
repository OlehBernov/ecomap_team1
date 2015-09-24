package com.ecomap.ukraine.authentication.manager;

import com.ecomap.ukraine.models.User;

/**
 * Interface of classes, which want to get
 * information about identification of user.
 */
public interface LogInListener {

    /**
     * Receive from server identified user.
     *
     * @param user identified user.
     */
    void onLogInResult(User user);

}
