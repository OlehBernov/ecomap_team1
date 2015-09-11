package com.ecomap.ukraine.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.Top10Item;
import com.ecomap.ukraine.ui.activities.ProblemDetailsActivity;
import com.ecomap.ukraine.ui.adapters.Top10ListAdapter;

import java.util.List;

/**
 * Created by Andriy on 10.09.2015.
 */
public class Top10ListFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String PROBLEM_EXTRA = "Problem";

    private Top10ListAdapter listAdapter;
    private List<Top10Item> top10ItemList;
    private int iconID;
    private ListView listView;
    private List<Problem> problems;


    public void setTop10ItemList(List<Top10Item> top10ItemList) {
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
        listView = (ListView)v.findViewById(R.id.list);
        listAdapter = new Top10ListAdapter(getActivity(), top10ItemList, iconID);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showProblemInformation(listAdapter.getItem(position).getProblemID());
    }

    public void showProblemInformation(int probemID) {
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
