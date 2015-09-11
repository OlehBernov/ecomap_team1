package com.ecomap.ukraine.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.AllTop10Items;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.Top10Item;
import com.ecomap.ukraine.problemupdate.manager.DataManager;
import com.ecomap.ukraine.problemupdate.manager.ProblemListener;
import com.ecomap.ukraine.ui.activities.ProblemDetailsActivity;
import com.ecomap.ukraine.ui.adapters.Top10ListAdapter;

import java.util.List;

/**
 * Created by Andriy on 10.09.2015.
 */
public class Top10ListFragment extends Fragment implements AdapterView.OnItemClickListener, ProblemListener {

    public static final String PROBLEM_EXTRA = "Problem";

    private Top10ListAdapter listAdapter;
    private List<Top10Item> top10ItemList;
    private int iconID;
    private ListView listView;
    private List<Problem> problems;


    public void setTop10ItemList(List<Top10Item> top10ItemList)  {
        this.top10ItemList = top10ItemList;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_top_10, container, false);
        DataManager.getInstance(getActivity()).registerProblemListener(this);
        listView = (ListView)v.findViewById(R.id.list);
        listAdapter = new Top10ListAdapter(getActivity(), top10ItemList, iconID);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DataManager.getInstance(getActivity()).removeProblemListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showProblemInformation(listAdapter.getItem(position).getProblemID());
    }

    public void showProblemInformation(int probemID) {
        if (problems == null) {
            Toast.makeText(getActivity(), "Error of loading try again.", Toast.LENGTH_LONG)
                    .show();
            DataManager.getInstance(getActivity()).getAllProblems();
        } else {
            Intent intent = new Intent(getActivity(), ProblemDetailsActivity.class);
            Problem currentProblem = problems.get(0);
            for (Problem problem : problems) {
                if (problem.getProblemId() == probemID) {
                    currentProblem = problem;
                    break;
                }
            }
            intent.putExtra(PROBLEM_EXTRA, currentProblem);
            startActivity(intent);
        }
    }

    @Override
    public void updateAllProblems(List<Problem> problems) {
        this.problems = problems;
    }

    @Override
    public void updateProblemDetails(Details details) {

    }

    @Override
    public void updateTop10(AllTop10Items allTop10Items) {
    }
}
