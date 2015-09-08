package com.ecomap.ukraine.util;


import android.content.Context;
import android.os.AsyncTask;

import com.ecomap.ukraine.problemupdate.manager.DataManager;
import com.ecomap.ukraine.models.Problem;

public class Refresher {

    public static void setRefreshTask(final Context context, final Problem problem) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DataManager.getInstance(context)
                        .refreshProblemDetails(problem.getProblemId());
                return null;
            }
        }.execute();
    }
}
