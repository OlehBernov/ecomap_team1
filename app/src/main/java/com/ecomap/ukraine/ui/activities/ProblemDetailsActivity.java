package com.ecomap.ukraine.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.problemupdate.manager.DataManager;
import com.ecomap.ukraine.problemupdate.manager.ProblemListener;
import com.ecomap.ukraine.ui.DetailsContent;

import java.util.List;

/**
 * Activity which displays problem details after click on recycle view element.
 */
public class ProblemDetailsActivity extends AppCompatActivity implements ProblemListener {

    private static final String PROBLEM_DETAILS_TITLE = "Problem Details";

    private DetailsContent detailsContent;
    private Problem problem;

    @Override
    public void updateAllProblems(List<Problem> problems) {
    }

    /**
     * Sets details of the problem to details view.
     *
     * @param details details of concrete problem.
     */
    @Override
    public void updateProblemDetails(Details details) {
        detailsContent.setProblemDetails(details);
    }

    @Override
    public void updateTop10(AllTop10Items allTop10Items) {

    }

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
        detailsContent = (DetailsContent) findViewById(R.id.search_details_content);
        detailsContent.setProblemContent(problem);

        DataManager dataManager = DataManager.getInstance(this);
        dataManager.registerProblemListener(this);
        dataManager.getProblemDetail(problem.getProblemId());
    }

    /**
     * Remove itself from DataManager observers.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataManager.getInstance(this).removeProblemListener(this);
    }

    /**
     * Sets toolbar on activity.
     */
    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.problem_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(PROBLEM_DETAILS_TITLE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
