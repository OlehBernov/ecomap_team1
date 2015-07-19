package com.ecomap.ukraine.temp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.activities.MainActivity;
import com.ecomap.ukraine.gui.elements.Problem;
import com.ecomap.ukraine.restclient.DataManager;
import com.ecomap.ukraine.restclient.RequestTypes;
import com.ecomap.ukraine.restclient.RestListener;

import java.util.List;
import java.util.Random;

public class Splashscreen2 extends Activity implements RestListener{

    private static int splashInterval = 2000;
    Intent intent = new Intent(this, MainActivity.class);
    boolean state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DataManager manager = DataManager.getInstance();
        intent.putExtra("randomProblem", "Connection error"); // Temp
        manager.registerListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashscreen);

        manager.getAllProblems(this);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                if (state) {
                    startActivity(intent);
                } else {
                    state = true;
                }
            }

        }, splashInterval);
    };

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
            if (state) {
                startActivity(intent);
            } else {
                state = true;
            }
        } else {
                //TODO Error handling
        }
    }
}
