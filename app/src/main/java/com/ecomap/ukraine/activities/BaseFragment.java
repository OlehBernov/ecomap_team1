package com.ecomap.ukraine.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.SupportMapFragment;
import com.ecomap.ukraine.R;

public abstract class BaseFragment extends Fragment {

    private SupportMapFragment mapFragment;
    protected GoogleMap map;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createMapFragmentIfNeeded();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void createMapFragmentIfNeeded() {
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = createMapFragment();
            FragmentTransaction tx = fm.beginTransaction();
            tx.add(R.id.map_container, mapFragment);
            tx.commit();
        }
    }

    protected SupportMapFragment createMapFragment() {
        return SupportMapFragment.newInstance();
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = mapFragment.getExtendedMap();
            if (map != null) {
                setUpMap();
            }
        }
    }

    protected abstract void setUpMap();
}