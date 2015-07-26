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
import com.ecomap.ukraine.data.manager.ProblemListener;
import com.ecomap.ukraine.database.DBHelper;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.data.manager.DataManager;

import java.util.List;

/**
 * Activity which represent loading data from server
 */
public class Splashscreen extends Activity implements ProblemListener {

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
     * Data manager instance
     */
    private  DataManager manager = DataManager.getInstance();;

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
        context = this.getApplicationContext();
        intent = new Intent(this, MainActivity.class);

        manager.setContext(context);
        manager.registerProblemListener(this);
     //   manager.registerDataListener(new DBHelper(getApplicationContext()));
        manager.getAllProblems();
    }

    /**
     * Initialize dialog interrupt downloads.
     */
    public  void setFailureLoadingDialog() {

        progressBar.setVisibility(View.INVISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Panel);
        builder.setCancelable(false);
        builder.setMessage(Splashscreen.FAILURE_OF_LOADING);
        builder.setPositiveButton(Splashscreen.RETRY,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        progressBar.setVisibility(View.VISIBLE);
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

    /**
     * Opens MainActivity
     * @param requestType
     * @param problem
     */
    @Override
    public void update(int requestType, Object problem) {
        if (problem != null) {
            if (!state) {
                //TODO: can we delete state?
                state = true;
                long endLoading = System.currentTimeMillis();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        manager.removeProblemListener(Splashscreen.this);
                        startActivity(intent);
                    }
                }, Math.max(MINIMAL_DELAY - (endLoading - startLoading), 0));
            }
        } else {
            setFailureLoadingDialog();
        }
    }
}
