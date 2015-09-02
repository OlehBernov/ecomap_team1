package com.ecomap.ukraine.activities.addProblem;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Adapter beetween activity and tabs fragments
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private int numbOfTabs;
    private Activity activity;

    /**
     * Constructor
     */
    public ViewPagerAdapter(FragmentManager fragmentManager, String[] titles, int numbOfTabs,
                            Activity activity) {
        super(fragmentManager);
        this.titles = titles;
        this.numbOfTabs = numbOfTabs;
        this.activity = activity;
    }

    /**
     * Get tab activity by position
     * @param position posiion of tab
     * @return tab activity
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return AddProblemDescriptionFragment.getInstance(null, null, activity);
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
     * Gets title of tabe from position
     * @param position posiion of tab
     * @return tite
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}