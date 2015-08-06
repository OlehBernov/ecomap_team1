package com.ecomap.ukraine.account.manager;

import com.ecomap.ukraine.models.User;

/**
 * TODO docs
 */
public interface LogInListenerNotifier {

    /**
     *
     *
     * @param listener
     */
    void registerLogInListener(final LogInListener listener);

    /**
     *
     *
     * @param listener
     */
    void removeLogInListener(final LogInListener listener);

    /**
     *
     *
     * @param user
     */
    void sendLogInResult(final User user);

}
