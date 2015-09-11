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
import android.view.View;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.filtration.Filter;
import com.ecomap.ukraine.filtration.FilterManager;
import com.ecomap.ukraine.filtration.FilterState;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.problemupdate.manager.DataManager;
import com.ecomap.ukraine.problemupdate.manager.ProblemListener;
import com.ecomap.ukraine.ui.adapters.ProblemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleh on 8/31/2015.
 */
public class SearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, ProblemListener {

    public static final String PROBLEM_EXTRA = "Problem";

    private RecyclerView recyclerView;
    private ProblemAdapter adapter;
    private List<Problem> problems;
    private List<Problem> unfilteredProblems;
    private FilterManager filterManager;

    public void showProblemInformation(Problem problem) {
        Intent intent = new Intent(this, ProblemDetailsActivity.class);
        intent.putExtra(PROBLEM_EXTRA, problem);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        registerSearchListener(menu);
        return true;
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

    @Override
    public void updateAllProblems(List<Problem> problems) {
        unfilteredProblems = problems;
        showProblemList();
        DataManager.getInstance(this).removeProblemListener(this);
    }

    @Override
    public void updateProblemDetails(Details details) {
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void requestProblemsList() {
        DataManager dataManager = DataManager.getInstance(this);
        dataManager.registerProblemListener(this);
        filterManager = FilterManager.getInstance(this);
        dataManager.getAllProblems();
    }

    private void registerSearchListener(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
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

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final List<Problem> problems = new ArrayList<>(this.problems);
        adapter = new ProblemAdapter(problems, this);
        recyclerView.setAdapter(adapter);
    }

    private void showProblemList() {
        FilterState filterState = filterManager.getCurrentFilterState();
        problems = new Filter().filterProblem(unfilteredProblems, filterState);
        setUpRecyclerView();
    }

}
