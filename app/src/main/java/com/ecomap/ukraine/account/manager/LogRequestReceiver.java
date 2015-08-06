package com.ecomap.ukraine.account.manager;

import com.ecomap.ukraine.models.User;

/**
 * Created by Alexander on 02.08.2015.
 */
public interface LogRequestReceiver {

    void setLogInRequestResult(User user);

}
