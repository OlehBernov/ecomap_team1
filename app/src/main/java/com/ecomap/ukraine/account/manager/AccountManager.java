package com.ecomap.ukraine.account.manager;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    private LogInClient logInClient;

    private Context context;

    private AccountManager(final Context context) {
        this.context = context;
        logInClient = new LogInClient(this, context);
    }

    public static AccountManager getInstance(final Context context) {
        if (instance == null) {
            instance = new AccountManager(context);
        }
        return instance;
    }

    @Override
    public void setLogInRequestResult(final User user) {
        sendLogInResult(user);
    }

    @Override
    public void registerLogInListener(final LogInListener listener) {
        logInListeners.add(listener);
    }

    @Override
    public void removeLogInListener(final LogInListener listener) {
        logInListeners.remove(listener);
    }

    @Override
    public void sendLogInResult(final User user) {
        for (LogInListener listener: logInListeners) {
            listener.setLogInResult(user);
        }
    }

    public void logInUser(final String password, final String login) {
        logInClient.postLogIn(password, login);
    }

    public void registerUser (final String firstname, final String lastname,
                              final String email, final String password) {
        logInClient.postRegistration(firstname, lastname, email, password);
    }


}
