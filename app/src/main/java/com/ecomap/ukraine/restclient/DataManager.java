package com.ecomap.ukraine.restclient;

import android.content.Context;

import java.util.HashSet;
import java.util.Observer;
import java.util.Set;

/**
 * Created by Oleh on 7/19/2015.
 */
public class DataManager {

    /**
     * Holds the Singleton global instance of DataManager.
     */
    private static DataManager instance = new DataManager();

    /**
     * Set of listeners.
     */
    public Set<RestListener> listeners = new HashSet<>();

    /**
     * Holds the reference to LoadingClient used by DataManager.
     */
    private LoadingClient loadingClient;

    /**
     * Initialize loading client and prevents this class from being
     * instantiated.
     */
    private DataManager() {
        loadingClient = new LoadingClient();
    }

    /**
     * Returns Singleton instance of DataManger
     */
    public static DataManager getInstance() {
        return instance;
    }

    /**
     * Adds the specified listener to the set of listeners. If it is already
     * registered, it is not added a second time.
     *
     * @param listener the DataListener to add.
     */
    public void registerListener(RestListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the specified listener from the set of listeners.
     *
     * @param listener the DataListener to remove.
     */
    public void removeListener(RestListener listener) {
        listeners.remove(listener);
    }

    /**
     * This method is used to made request for all problems.
     *
     * @param context the context that is to be used for request.
     */
    public void getAllProblems(Context context) {
        loadingClient.getAllProblems(context, listeners);
    }

    /**
     * This method is used to made request for more information about
     * the problem .
     *
     * @param problemId the id of the problem.
     * @param context   the context that is to be used for request.
     */
    public void getProblemDetail(int problemId, Context context) {
        loadingClient.getProblemDetail(problemId, context, listeners);
    }
}