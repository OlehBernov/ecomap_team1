package com.ecomap.ukraine.activities.main;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
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
public class SplashScreen extends Activity implements ProblemListener {

    /**
     * Failure loading message
     */
    private static final String FAILURE_OF_LOADING = "Please " +
            " ensure you`re connected to the Internet and try again.";

    private static final String FAILE_TITLE = "Failed to load.";

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
                        manager.removeProblemListener(SplashScreen.this);
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

        Context context = this.getApplicationContext();
        intent = new Intent(this, LoginScreen.class);

        manager = DataManager.getInstance(context);
        manager.registerProblemListener(this);
        manager.getAllProblems();
    }

    private long loadingTime(final long endLoading) {
        return MINIMAL_DELAY - (endLoading - startLoading);
    }

    /**
     * Initialize dialog interrupt downloads.
     */
    private void setFailureLoadingDialog() {
        smoothProgressBar.setVisibility(View.INVISIBLE);

        new MaterialDialog.Builder(this)
                .title(FAILE_TITLE)
                .content(SplashScreen.FAILURE_OF_LOADING)
                .backgroundColorRes(R.color.log_in_dialog)
                .contentColorRes(R.color.log_in_content)
                .negativeColorRes(R.color.log_in_content)
                .titleColorRes(R.color.log_in_title)
          .cancelable(false)
                .positiveText(SplashScreen.RETRY)
                .negativeText(SplashScreen.CANCEL).callback(
                        new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                smoothProgressBar.setVisibility(View.VISIBLE);
                                manager.getAllProblems();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                                System.exit(0);
                            }
                        })
                .show();
    }

}