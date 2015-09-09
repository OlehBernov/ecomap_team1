package com.ecomap.ukraine.ui.adapters;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ecomap.ukraine.ui.fragments.AddPhotoFragment;
import com.ecomap.ukraine.ui.fragments.AddProblemDescriptionFragment;

/**
 * Adapter between activity and tabs fragments
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private int numbOfTabs;
    /**
     * Constructor
     */
    public ViewPagerAdapter(FragmentManager fragmentManager, String[] titles, int numbOfTabs) {
        super(fragmentManager);
        this.titles = titles;
        this.numbOfTabs = numbOfTabs;
    }

    /**
     * Get tab activity by position
     * @param position position of tab
     * @return tab activity
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return AddProblemDescriptionFragment.getInstance(null, null);
        } else {
            return new AddPhotoFragment();
        }
    }

    /**
     * Gets number of tabs
     * @return number of tabs
     */
    @Override
    public int getCount() {
        return numbOfTabs;
    }

    /**
     * Gets title of tab from position
     * @param position position of tab
     * @return title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}