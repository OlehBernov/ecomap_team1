package com.ecomap.ukraine.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.DataListener;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.database.DBHelper;
import com.ecomap.ukraine.models.Problem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Andriy on 25.07.2015.
 */
public class FragmentEcoMap extends android.support.v4.app.Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    private DataManager manager;
    private static final  LatLng INTIAL_POSITION = new LatLng(48.4 , 31.2);


    public static FragmentEcoMap newInstance() {
        FragmentEcoMap fragment = new FragmentEcoMap();
        return fragment;
    }

    public FragmentEcoMap() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setUpMapIfNeeded();




        return rootView;
    }





    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = mMapView.getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
        }
    }
    private void setUpMap() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(INTIAL_POSITION.latitude, INTIAL_POSITION.longitude)).zoom(5).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings settings = googleMap.getUiSettings();
        googleMap.setMyLocationEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        putAllProblemsOnMap();
    }

    public void putAllProblemsOnMap () {
        DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext());
        List<Problem> list = dbHelper.getAllProblems();
        for (Problem problem : list) {
            // create marker
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(problem.getPosition().latitude, problem.getPosition().longitude)).title(problem.getTitle());
            BitmapDescriptor image = BitmapDescriptorFactory.fromResource(this.getResourceIdForMarker(
                    problem.getProblemTypesId()));
            marker.icon(image);
            // adding marker
            googleMap.addMarker(marker);

        }


    }

    private int getResourceIdForMarker (int typeId) {
        int resId = 0;
        switch (typeId) {
            case 1:
                resId = R.drawable.type1;
                break;
            case 2:
                resId = R.drawable.type2;
                break;
            case 3:
                resId = R.drawable.type3;
                break;
            case 4:
                resId = R.drawable.type4;
                break;
            case 5:
                resId = R.drawable.type5;
                break;
            case 6:
                resId = R.drawable.type6;
                break;
            case 7:
                resId = R.drawable.type7;
                break;

        }
        return resId;

    }



}
