package com.ecomap.ukraine.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.ecomap.ukraine.models.Statistics;
import com.ecomap.ukraine.ui.fragments.StatisticsFragment;
import com.ecomap.ukraine.update.manager.DataListenerAdapter;
import com.ecomap.ukraine.update.manager.DataManager;

/**
 * Adapter fot tabs which represents information about statistics of problem posting.
 */
public class StatisticsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private int numberOfTabs;
    private Statistics statistics;

    public StatisticsPagerAdapter(FragmentManager fragmentManager, String[] titles, int numberOfTabs,
                                  final Context context, final View progressView) {
        super(fragmentManager);

        this.titles = titles;
        this.numberOfTabs = numberOfTabs;

        DataManager.getInstance(context).registerDataListener(
                new DataListenerAdapter() {
                    @Override
                    public void onStatisticsUpdate(Statistics statistics) {
                        StatisticsPagerAdapter.this.statistics = statistics;
                        progressView.setVisibility(View.GONE);
                        DataManager.getInstance(context).removeDataListener(this);
                    }
                });
        DataManager.getInstance(context).getStatistics();
    }

    /**
     * Gets fragment which represents information about statistics of problem posting by position.
     *
     * @param position position of tab.
     * @return relevant fragment.
     */
    @Override
    public Fragment getItem(int position) {
        StatisticsFragment statisticsFragment = new StatisticsFragment();

        if (statistics == null) {
            statisticsFragment.setStatisticItem(null);
            return statisticsFragment;
        }

        switch (position) {
            case 0:
                statisticsFragment.setStatisticItem(statistics.getDailyStatistics());
                break;
            case 1:
                statisticsFragment.setStatisticItem(statistics.getWeeklyStatistics());
                break;
            case 2:
                statisticsFragment.setStatisticItem(statistics.getMonthStatistics());
                break;
            case 3:
                statisticsFragment.setStatisticItem(statistics.getAnnualStatistics());
                break;
            case 4:
                statisticsFragment.setStatisticItem(statistics.getStatisticsForAllTime());
                break;
            default:
                statisticsFragment.setStatisticItem(null);
                break;
        }

        return statisticsFragment;
    }

    /**
     * Gets number of tabs.
     *
     * @return number of tabs.
     */
    @Override
    public int getCount() {
        return numberOfTabs;
    }

    /**
     * Gets title of tab by position.
     *
     * @param position position of tab.
     * @return title of tab.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
