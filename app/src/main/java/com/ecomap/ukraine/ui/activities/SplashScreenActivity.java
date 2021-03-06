package com.ecomap.ukraine.ui.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.authentication.manager.AccountManager;
import com.ecomap.ukraine.update.manager.DataListenerAdapter;
import com.ecomap.ukraine.update.manager.DataManager;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.User;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Activity which represent loading data from server
 */
public class SplashScreenActivity extends Activity {

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
     * User
     */
    User user;

    private DataListenerAdapter dataListenerAdapter;

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

        Context context = getApplicationContext();
        intent = new Intent(this, LoginActivity.class);

        AccountManager accountManager = AccountManager.getInstance(getApplicationContext());
        user = accountManager.getUser();

        if(accountManager.isAnonymousUser()) {
            intent = new Intent(this, LoginActivity.class);
        }
        else {
            intent = new Intent(this, MainActivity.class);
        }

        manager = DataManager.getInstance(context);
        dataListenerAdapter = new DataListenerAdapter() {
            /**
             * Opens Main Activity.
             *
             * @param problems list of all problems.
             */
            @Override
            public void onAllProblemsUpdate(final List<Problem> problems) {
                if (problems != null) {
                    if (!state) {
                        state = true;
                        long endLoading = System.currentTimeMillis();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                manager.removeDataListener(dataListenerAdapter);
                                startActivity(intent);
                            }
                        }, Math.max(loadingTime(endLoading), 0));
                    }
                } else {
                    setFailureLoadingDialog();
                }
            }
        };
        manager.registerDataListener(dataListenerAdapter);
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
                .content(FAILURE_OF_LOADING)
                .backgroundColorRes(R.color.log_in_dialog)
                .contentColorRes(R.color.log_in_content)
                .negativeColorRes(R.color.log_in_content)
                .titleColorRes(R.color.log_in_title)
                .cancelable(false)
                .positiveText(RETRY)
                .negativeText(CANCEL).callback(
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
                        dialog.cancel();
                        finish();
                    }
                })
                .show();
    }

}