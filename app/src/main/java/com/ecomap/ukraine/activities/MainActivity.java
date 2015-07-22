package com.ecomap.ukraine.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.restclient.DataManager;
import com.ecomap.ukraine.restclient.RequestTypes;
import com.ecomap.ukraine.restclient.DataListener;

import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements DataListener{
    private DataManager manager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager.registerListener(this);
        ((TextView)findViewById(R.id.textView))
                .setText(getIntent()
                .getStringExtra("randomProblem"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(int requestType, Object requestResult) {
        switch (requestType) {
            case RequestTypes.ALL_PROBLEMS:
                showRandomProblem(requestResult);
        }
    }

    private void showRandomProblem(Object requestResult) {
        if (requestResult != null) {
            Random rand = new Random();
            List<Problem> problems = (List)requestResult;
            Problem problem = problems.get(rand.nextInt(problems.size()));
            ((TextView)findViewById(R.id.textView)).setText("" + problem.getTitle());
        } else {
            ((TextView)findViewById(R.id.textView)).setText("Connection error");
        }
    }
}
