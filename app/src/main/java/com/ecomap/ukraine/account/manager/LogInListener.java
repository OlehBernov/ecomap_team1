package com.ecomap.ukraine.account.manager;

import com.ecomap.ukraine.models.User;

/**
 * Interface of classes, which want to get
 * information about identidication of user.
 */
public interface LogInListener {

    /**
     * Receive from server identified user.
     *
     * @param user identified user.
     */
    void setLogInResult(User user);

}
