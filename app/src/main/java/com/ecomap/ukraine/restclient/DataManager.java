package com.ecomap.ukraine.restclient;

import android.content.Context;
import android.content.Intent;

import java.util.HashSet;

/**
 * Created by Oleh on 7/19/2015.
 */
public class DataManager {

    static DataManager instance = new DataManager();

    public HashSet<RestListener> listeners = new HashSet<>();

    private DataManager() {}

    public static DataManager getInstance() {
        return instance;
    }

    public void registerListener(RestListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RestListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners(int requestType, Object requestResult) {
        for (RestListener listener : listeners) {
            listener.send(requestType, requestResult);
        }
    }

    public void getAllProblems(Context context) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, RestConnector.class);
        intent.putExtra("RequestType", RequestTypes.ALL_PROBLEMS);
        context.startService(intent);
    }

    public void getProblemDetail(int problemId, Context context) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null,
                context, RestConnector.class);
        intent.putExtra("RequestType", RequestTypes.PROBLEM_DETAIL);
        intent.putExtra("Parameters", "" + problemId);
        context.startService(intent);
    }
}
