package com.ecomap.ukraine.activities.addProblem;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private int numbOfTabs;

    public ViewPagerAdapter(FragmentManager fragmentManager,
                            String[] titles, int numbOfTabs) {
        super(fragmentManager);
        this.titles = titles;
        this.numbOfTabs = numbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return AddProblemDescriptionFragment.getInstance(null, null);
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