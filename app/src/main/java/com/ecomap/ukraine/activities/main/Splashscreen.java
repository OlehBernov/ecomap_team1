package com.ecomap.ukraine.activities.main;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.activities.Authorization.LoginScreen;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.data.manager.ProblemListener;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Activity which represent loading data from server
 */
public class Splashscreen extends Activity implements ProblemListener {

    /**
     * Failure loading message
     */
    private static final String FAILURE_OF_LOADING = "Failed to load. Please " +
            " ensure you`re connected to the Internet and try again.";

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
     * Data manager instance
     */
    private DataManager manager;

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
     * Progress bar
     */
    private SmoothProgressBar smoothProgressBar;

    /**
     * Initialize activity
     *
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        smoothProgressBar = (SmoothProgressBar) findViewById(R.id.progress_bar);

        startLoading = System.currentTimeMillis();
        context = getApplicationContext();
        intent = new Intent(this, LoginScreen.class);

        manager = DataManager.getInstance(context);
        manager.registerProblemListener(this);
        manager.getAllProblems();
    }

    /**
     * Opens Main Activity.
     *
     * @param problems list of all problems.
     */
    @Override
    public void updateAllProblems(final List<Problem> problems) {
        if (problems != null) {
            if (!state) {
                state = true;
                long endLoading = System.currentTimeMillis();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        manager.removeProblemListener(Splashscreen.this);
                        startActivity(intent);
                    }
                }, Math.max(loadingTime(endLoading), 0));
            }
        } else {
            setFailureLoadingDialog();
        }
    }

    @Override
    public void updateProblemDetails(final Details details) {

    }

    private long loadingTime(final long endLoading) {
        return MINIMAL_DELAY - (endLoading - startLoading);
    }

    /**
     * Initialize dialog interrupt downloads.
     */
    private void setFailureLoadingDialog() {
        smoothProgressBar.setVisibility(View.INVISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Panel);
        builder.setCancelable(false);
        builder.setMessage(Splashscreen.FAILURE_OF_LOADING);
        builder.setPositiveButton(Splashscreen.RETRY,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        smoothProgressBar.setVisibility(View.VISIBLE);
                        manager.getAllProblems();
                    }
                });
        builder.setNegativeButton(Splashscreen.CANCEL,
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

}