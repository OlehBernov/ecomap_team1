package com.ecomap.ukraine.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidmapsextensions.ClusterGroup;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.data.manager.ProblemListener;
import com.ecomap.ukraine.models.Problem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;



/**
 * Created by Andriy on 25.07.2015.
 */
public class FragmentEcoMap extends BaseFragment implements ProblemListener {
    private DataManager manager = DataManager.getInstance();
    private static final  LatLng INTIAL_POSITION = new LatLng(48.4 , 31.2);
    private List<Marker> declusterifiedMarkers;


    public static FragmentEcoMap newInstance() {
        FragmentEcoMap fragment = new FragmentEcoMap();
        return fragment;
    }

    public FragmentEcoMap() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        manager.registerProblemListener(this);

        return inflater.inflate(R.layout.simple_map, container, false);
    }


    @Override
    protected void setUpMap() {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(INTIAL_POSITION.latitude, INTIAL_POSITION.longitude)).zoom(5).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);

        UiSettings settings = map.getUiSettings();
        map.setMyLocationEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        manager.getAllProblems();
        map.setClustering(new ClusteringSettings().clusterOptionsProvider(new DemoClusterOptionsProvider(getResources())));


    }

    public void putAllProblemsOnMap(List<Problem> problems) {
        MarkerOptions options = new MarkerOptions();
        for (Problem problem : problems) {
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(problem.getPosition().latitude, problem.getPosition().longitude)).title(problem.getTitle());
            BitmapDescriptor image = BitmapDescriptorFactory.fromResource(this.getResourceIdForMarker(
                    problem.getProblemTypesId()));
            marker.icon(image);
            map.addMarker(marker);

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

    @Override
    public void update(int requestType, Object problem) {
        putAllProblemsOnMap((List<Problem>) problem);
    }





}
