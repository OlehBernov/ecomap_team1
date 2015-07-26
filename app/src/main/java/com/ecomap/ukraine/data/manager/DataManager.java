package com.ecomap.ukraine.data.manager;

import android.content.Context;

import android.content.SharedPreferences;

import com.ecomap.ukraine.database.DBContract;
import com.ecomap.ukraine.database.DBHelper;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import com.ecomap.ukraine.updating.serverclient.LoadingClient;
import com.ecomap.ukraine.updating.serverclient.RequestTypes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Oleh on 7/19/2015.
 */
public class DataManager implements DataListenersNotifier, ProblemListenersNotifier {

    /**
     * Update frequency (one week in milliseconds).
     */
    private static final long ONE_WEEK = 604800000;

    /**
     * Holds the Singleton global instance of DataManager.
     */
    private static DataManager instance = new DataManager();

    /**
     * Set of server response listeners.
     */
    public Set<DataListener> dataListeners = new HashSet<>();

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
        registerDataListener(dbHelper);
        loadingClient = new LoadingClient(this, context);
    }

    /**
     * Returns Singleton instance of DataManger
     */
    public static DataManager getInstance() {
        return instance;
    }

    /**
     * Adds the specified listener to the set of dataListeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the DataListener to add.
     */
    public void registerDataListener(DataListener listener) {
        dataListeners.add(listener);
    }

    /**
     * Removes the specified listener from the set of dataListeners.
     *
     * @param listener the DataListener to remove.
     */
    public void removeDataListener(DataListener listener) {
        dataListeners.remove(listener);
    }

    /**
     * Notify all dataListeners about server response
     * and send them received information.
     *
     * @param requestType type of request.
     * @param requestResult server response converted to the objects of entities.
     */
    public void notifyDataListeners(final int requestType, Object requestResult) {
        for (DataListener listener : dataListeners) {
            listener.update(requestType, requestResult);
        }

        switch (requestType) {
            case RequestTypes.ALL_PROBLEMS:
                getAllProblems();
                break;
            case RequestTypes.PROBLEM_DETAIL:
                getProblemDetail(((Details) requestResult).getProblemId());
                break;
        }
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
     * Notify all ProblemListeners about data manger response
     * and send them received information.
     *
     * @param requestType request type.
     * @param problem result problem.
     */
    public void notifyProblemListeners(final int requestType, Object problem) {
        for (ProblemListener listener: problemListeners) {
            listener.update(requestType, problem);
        }
    }

    /**
     * This method is used to made request for all problems.
     */
    public void getAllProblems() {
        SharedPreferences settings = context.getSharedPreferences(DBContract.Problems.TIME, 0);
        long lastUpdateTime = settings.getLong(DBContract.Problems.TIME, 0);
        if (System.currentTimeMillis() - lastUpdateTime >= ONE_WEEK) {
            loadingClient.getAllProblems();
        } else {
            List<Problem> problems = dbHelper.getAllProblems();
            if (problems == null) {
                loadingClient.getAllProblems();
            } else {
                notifyProblemListeners(RequestTypes.ALL_PROBLEMS, problems);
            }
        }
    }

    /**
     * This method is used to made request for more information about
     * the problem.
     *
     * @param problemId the id of the problem.
     */
    public void getProblemDetail(int problemId) {
        long lastUpdateTime = Long.valueOf(dbHelper.getLastUpdateTime(problemId));
        if (System.currentTimeMillis() - lastUpdateTime >= ONE_WEEK) {
            loadingClient.getProblemDetail(problemId);
        } else {
            Details details = dbHelper.getProblemDetails(problemId);
            if (details == null) {
                loadingClient.getProblemDetail(problemId);
            } else {
                notifyProblemListeners(RequestTypes.PROBLEM_DETAIL, details);
            }
        }
    }

}

