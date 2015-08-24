package com.ecomap.ukraine.account.manager;

import com.ecomap.ukraine.models.User;

public interface LogRequestReceiver {

    void setLogInRequestResult(User user);

    void putUserToPreferences (User user, String password);

}
