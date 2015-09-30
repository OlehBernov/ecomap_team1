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
import android.widget.TextView;

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

    private static final int LEGEND_ITEM_SIZE = 12;
    private static final int VALUE_TEXT_SIZE = 14;
    private static final int ENTRY_SPACE = 7;
    private static final int SLICE_SPACE = 3;

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

        customizeLegend(pieChart.getLegend());

        if ((statisticItem == null) || isNotDataAvailable(statisticItem)) {
            pieChart.setVisibility(View.INVISIBLE);
            TextView noDataMessage = (TextView) v.findViewById(R.id.text_view_for_message);
            noDataMessage.setVisibility(View.VISIBLE);
        } else {
            setData(pieChart, loadColors());
        }

        return v;
    }

    /**
     * Sets statistics item.
     *
     * @param statisticItem new statistic item.
     */
    public void setStatisticItem(SparseIntArray statisticItem) {
        this.statisticItem = statisticItem;
    }

    /**
     * Loads colors for diagram slices from resources.
     *
     * @return list of colors for diagram slices.
     */
    private List<Integer> loadColors() {
        List<Integer> itemColors = new ArrayList<>();
        itemColors.add(getResources().getColor(R.color.forest_destruction));
        itemColors.add(getResources().getColor(R.color.rubbish_dump));
        itemColors.add(getResources().getColor(R.color.illegal_building));
        itemColors.add(getResources().getColor(R.color.water_pollution));
        itemColors.add(getResources().getColor(R.color.thread_to_biodiversity));
        itemColors.add(getResources().getColor(R.color.poaching));
        itemColors.add(getResources().getColor(R.color.other));

        return itemColors;
    }

    /**
     * Performs diagram legend settings.
     *
     * @param legend diagram legend.
     */
    private void customizeLegend(Legend legend) {
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setWordWrapEnabled(true);
        legend.setXEntrySpace(ENTRY_SPACE);
        legend.setYEntrySpace(ENTRY_SPACE);
        legend.setFormSize(LEGEND_ITEM_SIZE);
        legend.setTextSize(LEGEND_ITEM_SIZE);
        legend.setForm(Legend.LegendForm.SQUARE);
    }

    /**
     * Checks is statistics element have data.
     *
     * @param sparseIntArray statistics element.
     * @return is statistics element have data or not.
     */
    private boolean isNotDataAvailable(SparseIntArray sparseIntArray) {
        for (int i = 0; i < sparseIntArray.size(); i++) {
            if (sparseIntArray.get(i) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets data to the diagram.
     *
     * @param pieChart   diagram.
     * @param itemColors colors for diagram slices.
     */
    private void setData(PieChart pieChart, List<Integer> itemColors) {
        List<Entry> values = new ArrayList<>();
        for (int i = 0; i < statisticItem.size(); i++) {
            if (statisticItem.get(i) != 0) {
                values.add(new Entry(statisticItem.get(i), i));
            }
        }

        List<String> fieldNames = new ArrayList<>();
        for (int i = 0; i < statisticItem.size(); i++) {
            if (statisticItem.get(i) != 0) {
                fieldNames.add(getTitleById(i) + ": " + statisticItem.get(i));
            }
        }

        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < statisticItem.size(); i++) {
            if (statisticItem.get(i) != 0) {
                colors.add(itemColors.get(i));
            }
        }
        colors.add(ColorTemplate.getHoloBlue());

        PieDataSet pieDataSet = new PieDataSet(values, "");
        pieDataSet.setSliceSpace(SLICE_SPACE);
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(fieldNames, pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(VALUE_TEXT_SIZE);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    /**
     * Returns problem type title by id.
     *
     * @param id problem type id.
     * @return problem type title.
     */
    private String getTitleById(int id) {
        switch (id) {
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
