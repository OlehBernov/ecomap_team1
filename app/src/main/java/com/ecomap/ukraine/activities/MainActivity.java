package com.ecomap.ukraine.activities;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.ProblemListener;


import com.ecomap.ukraine.database.DBHelper;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Photo;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.updating.serverclient.RequestTypes;

import java.util.List;
import java.util.Random;

/**
 * Created by Andriy on 01.07.2015.
 *
 * Main activity, represent GUI and provides access to all functional
 *
 */

public class MainActivity extends ActionBarActivity implements ProblemListener {

    /**
     * Data manager instance.
     */
    private DataManager manager = DataManager.getInstance();

    /**
     * Initialize activity
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager.registerProblemListener(this);
        manager.getAllProblems();
    }

    /**
     * Inflate the menu, this adds items to the action bar if it is present.
     * @param menu activity menu
     * @return result of action
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *  Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     * @param item selected item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Update data from server
     * @param requestType the type of request handled.
     */
    @Override
    public void update(final int requestType, final Object problems) {
        switch (requestType) {
            case RequestTypes.ALL_PROBLEMS:
                showRandomProblem(problems);
                break;
            case RequestTypes.PROBLEM_DETAIL:
                Details details = (Details)problems;
                for (Photo photo : details.getPhotos().keySet()) {
                    new DBHelper(this).writeToFile(details.getPhotos().get(photo),
                            photo.getLink());
                    Bitmap image = new DBHelper(this).getBitmapByName(photo.getLink());
                  //  ((ImageView)findViewById(R.id.imageView2)).setImageBitmap(image);
                }
                break;
        }
    }

    /**
     * Show information about random problem
     * @param requestResult
     */
    private void showRandomProblem(Object requestResult) {
        if (requestResult != null) {
            Random rand = new Random();
            this.getFilesDir();
            List<Problem> problems = (List)requestResult;
            Problem problem = problems.get(rand.nextInt(problems.size()));
            ((TextView)findViewById(R.id.textView)).setText("" + problem.getTitle());
        } else {
            ((TextView)findViewById(R.id.textView)).setText("Connection error");
        }
    }
}
