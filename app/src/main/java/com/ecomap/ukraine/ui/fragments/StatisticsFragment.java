package com.ecomap.ukraine.ui.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ecomap.ukraine.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.List;


/**
 * Contains information about statistics of problem posting for a certain period.
 */
public class StatisticsFragment extends Fragment {

    public static final String FOREST_DESTRUCTION = "Forest destruction";
    public static final String RUBBISH_DUMP = "Rubbish dump";
    public static final String ILLEGAL_BUILDING = "Illegal building";
    public static final String WATER_POLLUTION = "Water pollution";
    public static final String THREAD_TO_BIODIVERSITY = "Thread to biodiversity";
    public static final String POACHING = "Poaching";
    public static final String OTHER = "Other";

    private SparseIntArray statisticItem;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statistics_layout, container, false);

        PieChart pieChart = (PieChart) v.findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawSliceText(false);
        pieChart.setDescription("");

        pieChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Legend legend = pieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setWordWrapEnabled(true);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(7f);
        legend.setFormSize(12f);
        legend.setTextSize(12f);
        legend.setForm(Legend.LegendForm.SQUARE);

        if (statisticItem != null) {
            setData(pieChart);
        }

        return v;
    }

    public void setStatisticItem(SparseIntArray statisticItem) {
        this.statisticItem = statisticItem;
    }

    private void setData(PieChart pieChart) {
        List<Entry> values = new ArrayList<>();
        for (int i = 0; i < statisticItem.size(); i++) {
            if (isItemHasValue(statisticItem.get(i))) {
                values.add(new Entry(statisticItem.get(i), i));
            }
        }

        PieDataSet pieDataSet = new PieDataSet(values, "");
        pieDataSet.setSliceSpace(3f);

        List<String> fieldNames = new ArrayList<>();
        for (int i = 0; i < statisticItem.size(); i++) {
            fieldNames.add(getTitleById(i) + ": " + statisticItem.get(i));
        }

        List<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.COLORFUL_COLORS) {
            colors.add(color);
        }

        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(fieldNames, pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(14f);

        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private boolean isItemHasValue(int item) {
        return item != 0;
    }

    private String getTitleById(int id) {
        switch(id) {
            case 0:
                return FOREST_DESTRUCTION;
            case 1:
                return RUBBISH_DUMP;
            case 2:
                return ILLEGAL_BUILDING;
            case 3:
                return WATER_POLLUTION;
            case 4:
                return THREAD_TO_BIODIVERSITY;
            case 5:
                return POACHING;
            case 6:
                return OTHER;
            default:
                return OTHER;
        }
    }

}
