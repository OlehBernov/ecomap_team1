package com.ecomap.ukraine.data.manager;

import android.content.Context;

import android.content.SharedPreferences;

import com.ecomap.ukraine.database.DBHelper;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import com.ecomap.ukraine.updating.serverclient.LoadingClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Coordinates the work of the database, data loading client and activities.
 * Provides updates of the database.
 */
public class DataManager implements ProblemListenersNotifier, RequestReceiver {

    /**
     * The name of the preference to retrieve.
     */
    private static final String TIME = "Time";

    /**
     * Update frequency (one week in milliseconds).
     */
    private static final long UPDATE_PERIOD = 604800000;

    /**
     * Holds the Singleton global instance of DataManager.
     */
    private static DataManager instance = new DataManager();

    /**
     * Set of problem listeners.
     */
    public Set<ProblemListener> problemListeners = new HashSet<>();

    /**
     * Holds the reference to LoadingClient used by DataManager.
     */
    private LoadingClient loadingClient;

    /**
     * Context of the application.
     */
    private Context context;

    /**
     * Helper in work with data base.
     */
    private DBHelper dbHelper;

    /**
     * Data manager constructor.
     */
    private DataManager() {}

    /**
     * Initialize loading client and database helper
     * and prevents this classes from being instantiated.
     * @param context context of the application.
     */
    public void setContext(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        loadingClient = new LoadingClient(this, context);
    }

    /**
     * Returns Singleton instance of DataManger
     */
    public static DataManager getInstance() {
        return instance;
    }

    /**
     * Adds the specified listener to the set of problemListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the ProblemListener to add.
     */
    public void registerProblemListener(ProblemListener listener) {
        problemListeners.add(listener);
    }

    /**
     * Removes the specified listener from the set of problemaListeners.
     *
     * @param listener the ProblemListener to remove.
     */
    public void removeProblemListener(ProblemListener listener) {
        problemListeners.remove(listener);
    }

    /**
     * Receives server response to the request of all problems,
     * and put it into database.
     *
     * @param problems list of all problems.
     */
    @Override
    public void setAllProblemsRequestResult(List<Problem> problems) {
        if (problems != null) {
            dbHelper.updateAllProblems(problems);
        }
        saveUpdateTime();

        getAllProblems();
    }

    /**
     * Receives server response to the request of details of
     * concrete problem, and put it into database.
     *
     * @param details details of concrete problem.
     */
    @Override
    public void setProblemDetailsRequestResult(Details details) {
        if (details != null) {
            dbHelper.updateProblemDetails(details);
        }

        getProblemDetail(details.getProblemId());
    }

    /**
     * Send to listeners list of all problems.
     */
    @Override
    public void sendAllProblems(List<Problem> problems) {
        for (ProblemListener listener: problemListeners) {
            listener.updateAllProblems(problems);
        }
    }

    /**
     * Send to listeners details of concrete problem.
     */
    @Override
    public void sendProblemDetails(Details details) {
        for (ProblemListener listener: problemListeners) {
            listener.updateProblemDetails(details);
        }
    }

    /**
     * This method is used to made request for all problems.
     * Initiates the updateAllProblems of brief information of the problem in the database,
     * if it is missing or obsolete.
     */
    public void getAllProblems() {
        SharedPreferences settings = context.getSharedPreferences(TIME, 0);
        long lastUpdateTime = settings.getLong(TIME, 0);
        if (isUpdateTime(lastUpdateTime)) {
            loadingClient.getAllProblems();
        } else {
            List<Problem> problems = dbHelper.getAllProblems();
            if (problems == null) {
                loadingClient.getAllProblems();
            } else {
                sendAllProblems(problems);
            }
        }
    }

    /**
     * This method is used to made request for more information about
     * the problem.
     * Initiates the updateAllProblems of detailed information of the problem
     * in the database, if it is missing or obsolete.
     *
     * @param problemId the id of the problem.
     */
    public void getProblemDetail(int problemId) {
        Details details = dbHelper.getProblemDetails(problemId);
        if (details == null) {
            loadingClient.getProblemDetail(problemId);
        } else {
            long lastUpdateTime = Long.valueOf(dbHelper.getLastUpdateTime(problemId));
            if (isUpdateTime(lastUpdateTime)) {
                loadingClient.getProblemDetail(problemId);
            } else {
                sendProblemDetails(details);
            }
        }
    }

    /**
     * Saves time of the last database update
     * to the SharedPreferences.
     */
    private void saveUpdateTime() {
        SharedPreferences settings = context.getSharedPreferences(TIME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(TIME, System.currentTimeMillis());
        editor.commit();
    }

    /**
     * Checks the need to update.
     *
     * @param lastUpdateTime time of the last database update.
     * @return the need to update.
     */
    private boolean isUpdateTime(long lastUpdateTime) {
        return (System.currentTimeMillis() - lastUpdateTime) >= UPDATE_PERIOD;
    }

}