package com.ecomap.ukraine.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.database.DBContract;
import com.ecomap.ukraine.models.Statistics;

import org.w3c.dom.Text;

/**
 * Contains information about statistics of problem posting for a certain period.
 */
public class StatisticsFragment extends Fragment {

    private SparseIntArray statisticItem;
    private TextView test;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statistics_layout, container, false);

        test = (TextView) v.findViewById(R.id.for_test);
        int text = 0;
        if (statisticItem != null) {
            text = statisticItem.get(2);
        }
        test.setText(String.valueOf(text));

        return v;
    }

    public void setStatisticItem(SparseIntArray statisticItem) {
        if (test != null) {
            test.setText(String.valueOf(statisticItem.get(2)));
        }
        this.statisticItem = statisticItem;
    }

}
