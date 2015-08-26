package com.ecomap.ukraine.account.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.ecomap.ukraine.account.client.LogInClient;
import com.ecomap.ukraine.activities.ExtraFieldNames;
import com.ecomap.ukraine.models.User;

import java.util.HashSet;
import java.util.Set;

public class AccountManager implements LogInListenerNotifier,
                                       LogRequestReceiver {


    private static AccountManager instance;
    private Set<LogInListener> logInListeners = new HashSet<>();
    private LogInClient logInClient;
    private static Context context;

    private AccountManager(final Context context) {
        AccountManager.context = context;
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
    public void putUserToPreferences(final User user, final String password) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ExtraFieldNames.USER_INFO,
                Context.MODE_PRIVATE).edit();
        editor.putString(ExtraFieldNames.LOGIN, user.getEmail());
        editor.putString(ExtraFieldNames.PASSWORD, password);
        editor.putInt(ExtraFieldNames.USER_ID, user.getId());
        editor.putString(ExtraFieldNames.USER_NAME, user.getName());
        editor.putString(ExtraFieldNames.USER_SURNAME, user.getSurname());
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

    public static void getUserFromPreference () {
        SharedPreferences userPreference = context.
                getSharedPreferences(ExtraFieldNames.USER_INFO, Context.MODE_PRIVATE);
        int userID = userPreference.getInt(ExtraFieldNames.USER_ID, -1);
        String userName = userPreference.getString(ExtraFieldNames.USER_NAME, "");
        String userSurname = userPreference.getString(ExtraFieldNames.USER_SURNAME, "");
        String email = userPreference.getString(ExtraFieldNames.LOGIN, "");
        User.newInstance(userID, userName, userSurname, "", "", "", email);
    }
}
