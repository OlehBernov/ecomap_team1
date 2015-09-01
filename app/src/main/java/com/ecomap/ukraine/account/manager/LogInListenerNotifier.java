package com.ecomap.ukraine.account.manager;

import com.ecomap.ukraine.models.User;

/**
 * Interface of class, which performs notification listeners
 * about readiness of information about identidication of user.
 */
public interface LogInListenerNotifier {

    /**
     * Adds the specified listener to the set of loginlisteners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the LogInListener to add.
     */
    void registerLogInListener(LogInListener listener);

    /**
     * Remove the specified listener from the set of loginlisteners.
     *
     * @param listener the LogInListener to remove.
     */
    void removeLogInListener(LogInListener listener);

    /**
     * Sends request result to listeners
     * @param user
     */
    void sendLogInResult(User user);

}
