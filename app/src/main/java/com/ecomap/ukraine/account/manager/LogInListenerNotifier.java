package com.ecomap.ukraine.account.manager;

import com.ecomap.ukraine.models.User;

public interface LogInListenerNotifier {

    void registerLogInListener(final LogInListener listener);

    void removeLogInListener(final LogInListener listener);

    void sendLogInResult(final User user);

}
