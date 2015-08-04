package com.ecomap.ukraine.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.Toolbar;

import com.ecomap.ukraine.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class MarkerListener implements GoogleMap.OnMarkerClickListener {

    private static final float ANCHOR_POINT = 0.7f;

    private Activity activity;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private Toolbar toolbar;

    public MarkerListener(Activity activity) {
        this.activity = activity;
        this.toolbar = (Toolbar) activity.findViewById(R.id.toolbar)  ;
        slidingUpPanelLayout = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        slidingUpPanelLayout.setAnchorPoint(ANCHOR_POINT);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        toolbar.setTitle("");
        return true;
    }

}
