package com.ecomap.ukraine.activities.problemDetails;


import android.app.Activity;
import android.os.AsyncTask;

import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.models.Problem;

public class Refresher {

    public void setRefreshTask(final Activity activity, final Problem problem) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DataManager.getInstance(activity)
                        .refreshProblemDetails(problem.getProblemId());
                return null;
            }
        }.execute();
    }
}
