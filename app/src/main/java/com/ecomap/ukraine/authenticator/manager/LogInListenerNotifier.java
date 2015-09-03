package com.ecomap.ukraine.authenticator.manager;

import com.ecomap.ukraine.model.User;

/**
 * Interface of class, which performs notification listeners
 * about readiness of information about identification of user.
 */
public interface LogInListenerNotifier {

    /**
     * Adds the specified listener to the set of login listeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the LogInListener to add.
     */
    void registerLogInListener(LogInListener listener);

    /**
     * Remove the specified listener from the set of login listeners.
     *
     * @param listener the LogInListener to remove.
     */
    void removeLogInListener(LogInListener listener);

    /**
     * Sends request result to listeners
     * @param user logged user
     */
    void sendLogInResult(User user);

}