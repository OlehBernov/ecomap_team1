package com.ecomap.ukraine.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.data.manager.ProblemListener;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

/**
 * Created by Andriy on 25.07.2015.
 */
public class FragmentEcoMap extends android.support.v4.app.Fragment implements ProblemListener {

    private MapView mMapView;
    private GoogleMap googleMap;
    private DataManager manager = DataManager.getInstance();
    private static final  LatLng INITIAL_POSITION = new LatLng(48.4 , 31.2);
    private ClusterManager<Problem> mClusterManager;

    public static FragmentEcoMap newInstance() {
        return new FragmentEcoMap();
    }

    public FragmentEcoMap() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        manager.registerProblemListener(this);

        View rootView = inflater.inflate(R.layout.fragement_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
            this.setUpMapIfNeeded();
        } catch (Exception e) {
            e.printStackTrace(); //TODO fix Andriy
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.removeProblemListener(this);
    }

    @Override
    public void updateAllProblems(List<Problem> problems) {
        putAllProblemsOnMap(problems);
    }

    @Override
    public void updateProblemDetails(Details details) {
        //TODO implement
    }

    private void setUpMapIfNeeded()  {
        if (googleMap == null) {
            googleMap = mMapView.getMap();
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings settings = googleMap.getUiSettings();
        googleMap.setMyLocationEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        manager.getAllProblems();

        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(new LatLng(INITIAL_POSITION.latitude, INITIAL_POSITION.longitude))
                .zoom(5)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);
    }

    public void putAllProblemsOnMap(List<Problem> problems) {
        mClusterManager = new ClusterManager<>(getActivity().getApplicationContext(), googleMap);
        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        for (Problem problem : problems) {
            mClusterManager.setRenderer(new IconRenderer(getActivity().getApplicationContext(), googleMap, mClusterManager));
            mClusterManager.addItem(problem);
        }
    }

}
