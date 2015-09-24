package com.ecomap.ukraine.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.update.manager.DataListenerAdapter;
import com.ecomap.ukraine.update.manager.DataManager;
import com.ecomap.ukraine.ui.fullinfo.DetailsContent;
import com.ecomap.ukraine.util.BasicContentLayout;

/**
 * Activity which displays problem details after click on recycle view element.
 */
public class ProblemDetailsActivity extends AppCompatActivity  {

    private DetailsContent detailsContent;
    private Problem problem;
    private DataListenerAdapter dataListenerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_problem_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            DataManager.getInstance(this).
                    refreshProblemDetails(problem.getProblemId());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Creates activity and sets base problem info on the details view.
     *
     * @param savedInstanceState the data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_details);
        setUpToolbar();

        problem = (Problem) getIntent().getSerializableExtra(SearchActivity.PROBLEM_EXTRA);
        LinearLayout detailsRoot = (LinearLayout) findViewById(R.id.pain2);
        BasicContentLayout basicContentLayout = new BasicContentLayout(detailsRoot);
        detailsContent = new DetailsContent(basicContentLayout, this);
        detailsContent.setBaseInfo(problem);

        DataManager dataManager = DataManager.getInstance(this);
        dataListenerAdapter = new DataListenerAdapter() {
            /**
             * Sets details of the problem to details view.
             *
             * @param details details of concrete problem.
             */
            @Override
            public void onProblemDetailsUpdate(Details details) {
                dataListenerAdapter = this;
                detailsContent.prepareToRefresh();
                detailsContent.setProblemDetails(details);
            }
        };
        dataManager.registerProblemListener(dataListenerAdapter);
        dataManager.getProblemDetail(problem.getProblemId());
    }

    /**
     * Remove itself from DataManager observers.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataManager.getInstance(this).removeProblemListener(dataListenerAdapter);
    }

    /**
     * Sets toolbar on activity.
     */
    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.problem_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
