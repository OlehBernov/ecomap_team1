package com.ecomap.ukraine.account.manager;

import com.ecomap.ukraine.models.User;

public interface LogRequestReceiver {

    void setLogInRequestResult(final User user);

    void putLogInResultToPreferences (final String password, final String login);

}
