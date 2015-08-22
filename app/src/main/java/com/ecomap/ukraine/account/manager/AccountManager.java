package com.ecomap.ukraine.account.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.ecomap.ukraine.account.client.LogInClient;
import com.ecomap.ukraine.models.User;

import java.util.HashSet;
import java.util.Set;

public class AccountManager implements LogInListenerNotifier,
                                       LogRequestReceiver {

    /**
     * The name of the preference to retrieve.
     */
    public static final String USER_INFO = "USER_INFO";
    public static final String LOGIN = "LOGIN";
    public static final String PASSWORD = "PASSWORD";
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
    public void putLogInResultToPreferences(final String password, final String login) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit();
        editor.putString(LOGIN, login);
        editor.putString(PASSWORD, password);
        editor.apply();
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
        for (LogInListener listener : logInListeners) {
            listener.setLogInResult(user);
        }
    }

    public void logInUser(final String password, final String login) {
        logInClient.postLogIn(password, login);
    }

    public void registerUser(final String firstname, final String lastname,
                             final String email, final String password) {
        logInClient.postRegistration(firstname, lastname, email, password);
    }
}
