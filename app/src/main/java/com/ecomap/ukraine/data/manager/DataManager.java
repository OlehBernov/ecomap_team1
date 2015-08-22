package com.ecomap.ukraine.data.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ecomap.ukraine.settings.UpdateTime;
import com.ecomap.ukraine.database.DBHelper;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.updating.serverclient.LoadingClient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Coordinates the work of the database, data loading client and activities.
 * Provides updates of the database.
 */
public class DataManager implements ProblemListenersNotifier,
                                    ProblemRequestReceiver {

    /**
     * The name of the preference to retrieve.
     */
    private static final String TIME = "Time";

    /**
     * Holds the Singleton global instance of DataManager.
     */
    private static DataManager instance;

    private static long updateTime = UpdateTime.ONCE_A_WEEK.getTimeInMilliseconds();

    /**
     * Set of problem listeners.
     */
    private Collection<ProblemListener> problemListeners = new CopyOnWriteArrayList<>();

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
    private DataManager(final Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        loadingClient = new LoadingClient(this, context);
    }

    /**
     * Returns Singleton instance of DataManger
     */
    public static DataManager getInstance(final Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    public static void setUpdateTime(UpdateTime updateTime) {
        DataManager.updateTime = updateTime.getTimeInMilliseconds();
        Log.e("update", "" + updateTime.getId());
    }

    /**
     * Adds the specified listener to the set of problemListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the ProblemListener to add.
     */
    public void registerProblemListener(final ProblemListener listener) {
        problemListeners.add(listener);
    }

    /**
     * Removes the specified listener from the set of problemaListeners.
     *
     * @param listener the ProblemListener to remove.
     */
    public void removeProblemListener(final ProblemListener listener) {
        problemListeners.remove(listener);
    }

    /**
     * Send to listeners list of all problems.
     */
    @Override
    public void sendAllProblems(final List<Problem> problems) {
        for (ProblemListener listener : problemListeners) {
            listener.updateAllProblems(problems);
        }
    }

    /**
     * Send to listeners details of concrete problem.
     */
    @Override
    public void sendProblemDetails(final Details details) {
        for (ProblemListener listener : problemListeners) {
            listener.updateProblemDetails(details);
        }
    }

    /**
     * Receives server response to the request of all problems,
     * and put it into database.
     *
     * @param problems list of all problems.
     */
    @Override
    public void setAllProblemsRequestResult(final List<Problem> problems) {
        if (problems != null) {
            dbHelper.updateAllProblems(problems);
            saveUpdateTime();
            getAllProblems();
        } else {
            sendAllProblems(null);
        }
    }

    /**
     * Receives server response to the request of details of
     * concrete problem, and put it into database.
     *
     * @param details details of concrete problem.
     */
    @Override
    public void setProblemDetailsRequestResult(final Details details) {
        if (details != null) {
            dbHelper.updateProblemDetails(details);
            getProblemDetail(details.getProblemId());
        } else {
            sendProblemDetails(null);
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
    public void getProblemDetail(final int problemId) {
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
        editor.apply();
    }

    /**
     * This method is used to made request for all problems.
     * Initiates the updateAllProblems of brief information of the problem in the database,
     * if it is missing or obsolete.
     */
    public void getAllProblems() {
        SharedPreferences settings = context.getSharedPreferences(TIME, Context.MODE_PRIVATE);
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
     * Checks the need to update.
     *
     * @param lastUpdateTime time of the last database update.
     * @return the need to update.
     */
    private boolean isUpdateTime(final long lastUpdateTime) {
        return (System.currentTimeMillis() - lastUpdateTime) >= updateTime;
    }

    public void refreshAllProblem() {
        loadingClient.getAllProblems();
    }

    public void refreshAllProblems() {
        SharedPreferences settings = context.getSharedPreferences(TIME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(TIME, 0);
        editor.apply();
        getAllProblems();
    }

    public void refreshProblemDetails(int problemId) {
//        registerProblemListener(listener);
        loadingClient.getProblemDetail(problemId);
    }

}