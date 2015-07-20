package com.ecomap.ukraine.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.gui.elements.Problem;
import com.ecomap.ukraine.restclient.DataManager;
import com.ecomap.ukraine.restclient.RequestTypes;
import com.ecomap.ukraine.restclient.RestListener;

import java.util.List;
import java.util.Random;

public class Splashscreen extends Activity implements RestListener{

    private static final  String FAILURE_OF_LOADING = "Failed to load. Please ensure you`re connected " +
            "to the Internet and try again.";
    private static final String RETRY = "Retry";
    private static final String CANCEL = "Cancel";
    private static final int MINIMAL_DELAY = 2000;

    private final Context context = this;
    private final DataManager dataManager = DataManager.getInstance();

    private Intent intent;
    private AnimationDrawable animationDrawable;
    private ImageView fourSquare;
    private boolean state = false;

    private long startLoadingTime;
    private long endLoadingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager.registerListener(this);
        startLoadingTime = System.currentTimeMillis();
        setContentView(R.layout.splashscreen);

        intent = new Intent(this, MainActivity.class);

        fourSquare = (ImageView) findViewById(R.id.fourSquare);
        animationDrawable = (AnimationDrawable) fourSquare.getDrawable();
        animationDrawable.start();

        dataManager.getAllProblems(this);
    }

    public  void setFailureLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_Holo_Light_Panel);

        builder.setCancelable(false);
        builder.setMessage(com.ecomap.ukraine.activities.Splashscreen.FAILURE_OF_LOADING);
        builder.setPositiveButton(com.ecomap.ukraine.activities.Splashscreen.RETRY,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                                        final int id) {

                        dataManager.getAllProblems(context);
                    }
                });
        builder.setNegativeButton(com.ecomap.ukraine.activities.Splashscreen.CANCEL,
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

    @Override
    public void update(int requestType, Object requestResult) {
        switch (requestType) {
            case RequestTypes.ALL_PROBLEMS:
                initialMainText(requestResult);
        }
    }

    private void initialMainText(Object requestResult) {
        if (requestResult != null) {
            Random rand = new Random();
            List<Problem> problems = (List)requestResult;
            Problem problem = problems.get(rand.nextInt(problems.size()));
            intent.putExtra("randomProblem", problem.getTitle());
            if (!state) {
                state = true;
                endLoadingTime = System.currentTimeMillis();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                }, Math.max(MINIMAL_DELAY - (endLoadingTime - startLoadingTime), 0));
            }
        } else {
            setFailureLoadingDialog();
        }
    }
}
