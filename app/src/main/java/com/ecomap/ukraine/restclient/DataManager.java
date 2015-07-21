package com.ecomap.ukraine.restclient;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleh on 7/19/2015.
 */
public class DataManager {

    private static DataManager instance = new DataManager();

    public Set<RestListener> listeners = new HashSet<>();
    private LoadingClient loadingClient;

    private DataManager() {
        loadingClient = new LoadingClient();
    }

    public static DataManager getInstance() {
        return instance;
    }

    public void registerListener(RestListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RestListener listener) {
        listeners.remove(listener);
    }

    public void getAllProblems(Context context) {
        loadingClient.getAllProblems(context, listeners);
    }

    public void getProblemDetail(int problemId, Context context) {
        loadingClient.getProblemDetail(problemId, context, listeners);
    }
}