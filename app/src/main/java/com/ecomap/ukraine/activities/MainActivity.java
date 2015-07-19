package com.ecomap.ukraine.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.gui.elements.Problem;
import com.ecomap.ukraine.restclient.DataManager;
import com.ecomap.ukraine.restclient.RequestTypes;
import com.ecomap.ukraine.restclient.RestListener;

import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements RestListener{
    DataManager manager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager.registerListener(this);
        manager.getAllProblems(this);
        (Toast.makeText(this, "???????", Toast.LENGTH_LONG)).show();
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
    public void send(int requestType, Object requestResult) {
        switch (requestType) {
            case RequestTypes.ALL_PROBLEMS:
                showRandomProblem(requestResult);
        }
    }

    private void showRandomProblem(Object requestResult) {
        Random rand = new Random(47);

        List<Problem> problems = (List)requestResult;
        Problem problem = problems.get(rand.nextInt(problems.size()));
        if (requestResult != null) {
            ((TextView)findViewById(R.id.textView)).setText("" + problem.getTitle());
        } else {
            ((TextView)findViewById(R.id.textView)).setText("Connection error");
        }
    }
}
