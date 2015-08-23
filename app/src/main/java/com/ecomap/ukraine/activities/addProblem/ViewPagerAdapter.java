package com.ecomap.ukraine.activities.addProblem;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private int numbOfTabs;
    private Activity activity;

    public ViewPagerAdapter(FragmentManager fragmentManager, String[] titles, int numbOfTabs,
                            Activity activity) {
        super(fragmentManager);
        this.titles = titles;
        this.numbOfTabs = numbOfTabs;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return AddProblemDescriptionFragment.getInstance(null, null, activity);
        } else {
            return new AddPhotoFragment();
        }
    }

    @Override
    public int getCount() {
        return numbOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}