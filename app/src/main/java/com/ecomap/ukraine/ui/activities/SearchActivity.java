package com.ecomap.ukraine.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.ui.adapters.ProblemAdapter;
import com.ecomap.ukraine.problemupdate.manager.DataManager;
import com.ecomap.ukraine.problemupdate.manager.ProblemListener;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleh on 8/31/2015.
 */
public class SearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, ProblemListener {

    public static final String PROBLEM_EXTRA = "Problem";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProblemAdapter adapter;
    private List<Problem> problems;

    public void showProblemInformation(Problem problem) {
        Intent intent = new Intent(this, ProblemDetailsActivity.class);
        intent.putExtra(PROBLEM_EXTRA, problem);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpToolbar();
        requestProblemsList();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
    }

    private void requestProblemsList() {
        DataManager dataManager = DataManager.getInstance(this);
        dataManager.registerProblemListener(this);
        dataManager.getAllProblems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        registerSearchListener(menu);
        return true;
    }

    private void registerSearchListener(Menu menu) {MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Problem> filteredProblems = filter(problems, newText);
        adapter.renewList(filteredProblems);
        recyclerView.scrollToPosition(0);
        return true;
    }

    private List<Problem> filter(List<Problem> problems, String textToSearch) {
        textToSearch = textToSearch.toLowerCase();
        List<Problem> result = new ArrayList<>();
        for (Problem problem : problems) {
            String title = problem.getTitle().toLowerCase();
            if (title.contains(textToSearch)) {
                result.add(problem);
            }
        }
        return result;
    }

    @Override
    public void updateAllProblems(List<Problem> problems) {
        this.problems = problems;
        setUpRecyclerView();
        DataManager.getInstance(this).removeProblemListener(this);
    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final List<Problem> problems = new ArrayList<>(this.problems);
        adapter = new ProblemAdapter(problems, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateProblemDetails(Details details) {
    }

}
