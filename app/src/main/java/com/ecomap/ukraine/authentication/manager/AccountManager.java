package com.ecomap.ukraine.authentication.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.ecomap.ukraine.authentication.LogInClient;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.util.ExtraFieldNames;

import java.util.HashSet;
import java.util.Set;

/**
 * Coordinates the work of the identification client and activities.
 */
public class AccountManager implements LogInResponseReceiver {

    /**
     * Holds the Singleton global instance of AccountManager.
     */
    private static AccountManager instance;

    /**
     * Set of listeners.
     */
    private Set<LogInListener> logInListeners = new HashSet<>();

    /**
     * Holds the reference to LoginClient used by AccountManager.
     */
    private LogInClient logInClient;

    /**
     * Application context
     */
    private Context context;

    /**
     * Constructor
     *
     * @param context application context
     */
    private AccountManager(final Context context) {
        this.context = context;
        logInClient = new LogInClient(this, context);
    }

    /**
     * Returns Singleton instance of AccountManager
     */
    public static AccountManager getInstance(final Context context) {
        if (instance == null) {
            synchronized (AccountManager.class) {
                if (instance == null) {
                    instance = new AccountManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * Checks if user is anonym
     */
    public boolean isAnonymousUser() {
        User user = getUser();
        return user.getId() < 0;
    }

    /**
     * Receives server response to the request identification
     *
     * @param user registered user
     */
    @Override
    public void getLogInResponseResult(final User user) {
        putUser(user);
        sendLogInResult(user);
    }

    /**
     * Puts user information to Shared Preferences
     *
     * @param user     user information
     */
    public void putUser(final User user) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ExtraFieldNames.USER_INFO,
                Context.MODE_PRIVATE).edit();
        editor.putString(ExtraFieldNames.LOGIN, user.getEmail());
        editor.putInt(ExtraFieldNames.USER_ID, user.getId());
        editor.putString(ExtraFieldNames.USER_NAME, user.getName());
        editor.putString(ExtraFieldNames.USER_SURNAME, user.getSurname());
        editor.apply();
    }

    /**
     * Adds the specified listener to the set of login listeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the LogInListener to add.
     */
    public void registerLogInListener(final LogInListener listener) {
        logInListeners.add(listener);
    }

    /**
     * Remove the specified listener from the set of login listeners.
     *
     * @param listener the LogInListener to remove.
     */
    public void removeLogInListener(final LogInListener listener) {
        logInListeners.remove(listener);
    }

    /**
     * Sends request result to listeners
     *
     * @param user logged user.
     */
    public void sendLogInResult(final User user) {
        for (LogInListener listener : logInListeners) {
            listener.onLogInResult(user);
        }
    }

    /**
     * Identify user on server
     *
     * @param password user password
     * @param login    user login
     */
    public void logInUser(final String password, final String login) {
        logInClient.postLogIn(password, login);
    }

    /**
     * Register user on server
     *
     * @param name     user name
     * @param surname  user surname
     * @param email    user email
     * @param password user password
     */
    public void registerUser(final String name, final String surname,
                             final String email, final String password) {
        logInClient.postRegistration(name, surname, email, password);
    }

    /**
     * Gets user information from Shared Preferences
     */
    public User getUser() {
        SharedPreferences userPreference = context.
                getSharedPreferences(ExtraFieldNames.USER_INFO, Context.MODE_PRIVATE);
        int userID = userPreference.getInt(ExtraFieldNames.USER_ID, -1);
        String userName = userPreference.getString(ExtraFieldNames.USER_NAME, "Anonym");
        String userSurname = userPreference.getString(ExtraFieldNames.USER_SURNAME, "");
        String email = userPreference.getString(ExtraFieldNames.LOGIN, "");
        return new User(userID, userName, userSurname, "", "", "", email);
    }

}
