package com.ecomap.ukraine.account.manager;

import android.content.Context;

import com.ecomap.ukraine.account.client.LogInClient;
import com.ecomap.ukraine.models.User;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class AccountManager implements LogInListenerNotifier,
                                       LogRequestReceiver {

    private static AccountManager instance;

    private Set<LogInListener> logInListeners = new HashSet<>();

    private Set<LogOutListener> logOutListeners = new HashSet<>();

    private LogInClient logInClient;

    private Context context;

    private AccountManager(Context context) {
        this.context = context;
        logInClient = new LogInClient(this, context);
    }

    public static AccountManager getInstance(Context context) {
        if (instance == null) {
            instance = new AccountManager(context);
        }
        return instance;
    }

    @Override
    public void setLogInRequestResult(User user) {
        sendLogInResult(user);
    }

    @Override
    public void setLogOutRequestResult(boolean success) {
        sendLogOutResult(success);
    }

    @Override
    public void registerLogInListener(LogInListener listener) {
        logInListeners.add(listener);
    }

    @Override
    public void removeLogInListener(LogInListener listener) {
        logInListeners.remove(listener);
    }

    @Override
    public void registerLogOutListener(LogOutListener listener) {
        logOutListeners.add(listener);
    }

    @Override
    public void removeLogOutListener(LogOutListener listener) {
        logOutListeners.remove(listener);
    }

    @Override
    public void sendLogInResult(User user) {
        for (LogInListener listener: logInListeners) {
            listener.setLogInResult(user);
        }
    }

    @Override
    public void sendLogOutResult(boolean success) {
        for (LogOutListener listener: logOutListeners) {
            listener.setLogOutResult(success);
        }
    }

    public void logInUser(String password, String login) {
        logInClient.postLogIn(password, login);
    }

    public void logOutUser() {
        logInClient.getLogOut();
    }

}
