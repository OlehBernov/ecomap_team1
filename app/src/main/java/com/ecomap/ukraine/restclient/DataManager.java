package com.ecomap.ukraine.restclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.util.HashSet;

/**
 * Created by Oleh on 7/19/2015.
 */
public class DataManager extends ResultReceiver {

    static DataManager instance = new DataManager();

    public HashSet<RestListener> listeners = new HashSet<>();

    private RestClient restClient;

    private Object requestResult;

    private DataManager() {
        super(new Handler());
        restClient = new RestClient();
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        notifyListeners(resultCode);
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

    public void setRequestResult(Object requestResult) {
        this.requestResult = requestResult;
    }

    public void notifyListeners(int requestType) {
        for (RestListener listener : listeners) {
            listener.update(requestType, requestResult);
        }
    }

    public void getAllProblems(Context context) {
        restClient.getAllProblems(context);
    }

    public void getProblemDetail(int problemId, Context context) {
      // restClient.getAllProblemDetail();
    }
}
