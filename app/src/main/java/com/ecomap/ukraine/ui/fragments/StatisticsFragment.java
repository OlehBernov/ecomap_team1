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

import java.util.ArrayList;
import java.util.List;

/*import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;*/

/**
 * Contains information about statistics of problem posting for a certain period.
 */
public class StatisticsFragment extends Fragment {

    private SparseIntArray statisticItem;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statistics_layout, container, false);

        /*PieChartView pieChartView = (PieChartView) v.findViewById(R.id.pie_chart_view);
        pieChartView.setInteractive(true);
        pieChartView.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL);

        List<SliceValue> sliceValues = new ArrayList<>();
        for (int i = 0; i < statisticItem.size(); i++) {
            sliceValues.add(new SliceValue(statisticItem.get(i)));
        }

        PieChartData pieChartData = new PieChartData();
        pieChartData.setValues(sliceValues);
        pieChartView.setPieChartData(pieChartData);*/

        return v;
    }

    public void setStatisticItem(SparseIntArray statisticItem) {
        this.statisticItem = statisticItem;
    }

}
