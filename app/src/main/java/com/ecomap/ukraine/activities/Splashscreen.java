package com.ecomap.ukraine.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.serverclient.DataManager;
import com.ecomap.ukraine.serverclient.RequestTypes;
import com.ecomap.ukraine.serverclient.DataListener;

import java.util.List;
import java.util.Random;

/**
 * Activity which represent loading data from server
 */
public class SplashScreen extends Activity implements DataListener{

    /**
     * Failure loading message
     */
    private static final  String FAILURE_OF_LOADING = "Failed to load. Please ensure you`re connected " +
            "to the Internet and try again.";
    /**
     * Retry button title
     */
    private final static String RETRY = "Retry";
    /**
     * Cancel button title
     */
    private final static String CANCEL = "Cancel";
    /**
     * Minimal time interval of loading
     */
    private final static int MINIMAL_DELAY = 2000;

    /**
     * Data manager instanse
     */
    private final DataManager manager = DataManager.getInstance();

    /**
     * Context of activity
     */
    private Context context;
    /**
     * MainActivity intent
     */
    private Intent intent;
    /**
     * State of loading
     */
    private boolean state = false;

    /**
     * Time of start loading
     */
    private long startLoading;
    /**
     * Time of end loading
     */
    private long endLoading;

    /**
     * Progress bar
     */
    private ProgressBar progressBar;

    /**
     * Initialize activity
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

        startLoading = System.currentTimeMillis();

        context = getApplicationContext();
        intent = new Intent(this, MainActivity.class);

        manager.registerListener(this);
        manager.getAllProblems(context);
    }

    /**
     * Initialize dialog interrupt downloads
     */
    public  void setFailureLoadingDialog() {

        progressBar.setVisibility(View.INVISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Panel);
        builder.setCancelable(false);
        builder.setMessage(SplashScreen.FAILURE_OF_LOADING);
        builder.setPositiveButton(SplashScreen.RETRY,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        progressBar.setVisibility(View.VISIBLE);
                        manager.getAllProblems(context);
                    }
                });
        builder.setNegativeButton(SplashScreen.CANCEL,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        System.exit(0);
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    /**
     * Update data from server
     * @param requestType the type of request handled.
     * @param requestResult the result of request.
     */
    @Override
    public void update(int requestType, Object requestResult) {
        switch (requestType) {
            case RequestTypes.ALL_PROBLEMS:
                initialMainText(requestResult);
        }
    }

    /**
     * Put information about problem to MainActivity
     * @param requestResult the result of request.
     */
    private void initialMainText(Object requestResult) {
        if (requestResult != null) {
            Random rand = new Random();
            List<Problem> problems = (List)requestResult;
            Problem problem = problems.get(rand.nextInt(problems.size()));
            intent.putExtra("randomProblem", problem.getTitle());
            if (!state) {
                state = true;
                endLoading = System.currentTimeMillis();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                }, Math.max(MINIMAL_DELAY - (endLoading - startLoading), 0));
            }
        } else {
            setFailureLoadingDialog();
        }
    }
}
