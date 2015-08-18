package com.ecomap.ukraine.activities.problemDetails;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class PhotoPagerAdapter extends PagerAdapter {

    private List<View> pages = null;

    public PhotoPagerAdapter(List<View> pages) {
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public void startUpdate(ViewGroup arg0) {
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View v = pages.get(position);
        collection.addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public void finishUpdate(ViewGroup arg0) {
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

}
