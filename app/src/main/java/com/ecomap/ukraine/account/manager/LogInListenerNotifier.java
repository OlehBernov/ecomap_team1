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
    void registerLogInListener(LogInListener listener);

    /**
     *
     *
     * @param listener
     */
    void removeLogInListener(LogInListener listener);

    /**
     *
     * @param listener
     */
    void registerLogOutListener(LogOutListener listener);

    /**
     *
     * @param listener
     */
    void removeLogOutListener(LogOutListener listener);

    /**
     *
     *
     * @param user
     */
    void sendLogInResult(User user);

    /**
     * TODO
     *
     * @param success
     */
    void sendLogOutResult(boolean success);

}
