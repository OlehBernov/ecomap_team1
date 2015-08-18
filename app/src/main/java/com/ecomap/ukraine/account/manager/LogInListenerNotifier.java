package com.ecomap.ukraine.account.manager;

import com.ecomap.ukraine.models.User;

public interface LogInListenerNotifier {

    void registerLogInListener(LogInListener listener);

    void removeLogInListener(LogInListener listener);

    void sendLogInResult(User user);

}
