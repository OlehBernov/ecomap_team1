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
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.Top10FragmentID;
import com.ecomap.ukraine.models.Top10Item;
import com.ecomap.ukraine.update.manager.DataListenerAdapter;
import com.ecomap.ukraine.update.manager.DataManager;
import com.ecomap.ukraine.ui.activities.ProblemDetailsActivity;
import com.ecomap.ukraine.ui.adapters.Top10ListAdapter;

import java.util.List;

/**
 * Created by Andriy on 10.09.2015.
 */
public class Top10ListFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String PROBLEM_EXTRA = "Problem";

    private int top10FragmentID;
    private Top10ListAdapter listAdapter;
    private List<Top10Item> top10ItemList;
    private int iconID;
    private ListView listView;
    private List<Problem> problems;
    private DataListenerAdapter dataListenerAdapter;
    private Top10ListFragment fragment = this;

    public void setTop10ItemList(List<Top10Item> top10ItemList)  {
        this.top10ItemList = top10ItemList;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public void setTop10FragmentID(int top10FragmentID) {
        this.top10FragmentID = top10FragmentID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_top_10, container, false);
        dataListenerAdapter = new DataListenerAdapter() {

            /**
             * Receive list of all problems.
             * @param updateProblems list of all problems.
             */
            @Override
            public void onAllProblemsUpdate(List<Problem> updateProblems) {
                problems = updateProblems;
            }

            /**
             * Receive oject, which contains top10 elements
             * @param allTop10Items object, which contains top 10 elements
             */
            @Override
            public void onTop10Update(AllTop10Items allTop10Items) {
                switch (top10FragmentID) {
                    case Top10Item.TOP_LIKE_FRAGMENT_ID:
                        top10ItemList = allTop10Items.getMostLikedProblems();
                        break;
                    case Top10Item.TOP_VOTE_FRAGMENT_ID:
                        top10ItemList = allTop10Items.getMostPopularProblems();
                        break;
                    case Top10Item.TOP_SEVERITY_FRAGMENT_ID:
                        top10ItemList = allTop10Items.getMostImportantProblems();
                        break;
                }
                listAdapter = new Top10ListAdapter(getActivity(), top10ItemList, iconID);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(fragment);
            }
        };
        DataManager.getInstance(getActivity()).registerProblemListener(dataListenerAdapter);
        listView = (ListView)v.findViewById(R.id.list);
        listAdapter = new Top10ListAdapter(getActivity(), top10ItemList, iconID);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DataManager.getInstance(getActivity()).removeProblemListener(dataListenerAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showProblemInformation(listAdapter.getItem(position).getProblemID());
    }

    public void showProblemInformation(int problemID) {
        if (problems == null) {
            Toast.makeText(getActivity(), "Error of loading try again.", Toast.LENGTH_LONG)
                    .show();
            DataManager.getInstance(getActivity()).getAllProblems();
        } else {
            Intent intent = new Intent(getActivity(), ProblemDetailsActivity.class);
            Problem currentProblem = problems.get(0);
            for (Problem problem : problems) {
                if (problem.getProblemId() == problemID) {
                    currentProblem = problem;
                    break;
                }
            }
            intent.putExtra(PROBLEM_EXTRA, currentProblem);
            startActivity(intent);
        }
    }

}
