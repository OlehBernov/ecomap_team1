package com.ecomap.ukraine.data.manager;

import android.content.Context;
import android.util.Log;

import com.ecomap.ukraine.database.DBContract;
import com.ecomap.ukraine.database.DBHelper;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.ProblemActivity;
import com.ecomap.ukraine.updating.serverclient.LoadingClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Oleh on 7/19/2015.
 */
public class DataManager implements ListenersNotifier {

    /**
     * Holds the Singleton global instance of DataManager.
     */
    private static DataManager instance = new DataManager();

    
    /**
     * Set of listeners.
     */
    public Set<DataListener> listeners = new HashSet<>();

    /**
     * Holds the reference to LoadingClient used by DataManager.
     */
    private LoadingClient loadingClient;

    /**
     * Initialize loading client and prevents this class from being
     * instantiated.
     */
    private DataManager() {
        loadingClient = new LoadingClient(this);
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
    public void registerListener(DataListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the specified listener from the set of listeners.
     *
     * @param listener the DataListener to remove.
     */
    public void removeListener(DataListener listener) {
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

    /**
     * Notify all listeners about server response
     * and send them received information.
     *
     * @param requestType type of request.
     * @param requestResult server response converted to the objects of entities.
     * @param listeners objects, which get response from
     *                  server converted to the objects of entities.
     */
    public void notifyListeners(final int requestType, Object requestResult,
                                 final Set<DataListener> listeners) {
        for (DataListener listener : listeners) {
            listener.update(requestType, requestResult);
        }
    }
}