package com.ecomap.ukraine.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.data.manager.DataManager;
import com.ecomap.ukraine.data.manager.ProblemListener;
import com.ecomap.ukraine.filter.FilterListener;
import com.ecomap.ukraine.filter.FilterManager;
import com.ecomap.ukraine.filter.FilterState;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Andriy on 25.07.2015.
 */
public class FragmentEcoMap extends android.support.v4.app.Fragment
        implements ProblemListener, FilterListener {

    private MapView mapView;
    private GoogleMap googleMap;
    private DataManager manager;
    private static final LatLng INITIAL_POSITION = new LatLng(48.4, 31.2);
    private static final float INITIAL_ZOOM = 5;
    private ClusterManager<Problem> clusterManager;
    private FilterState filterState;
    private static List<Problem> problems;

    /**
     * Filter manager instance
     */
    private FilterManager fmanager;


    /**
     * The name of the preference to retrieve.
     */
    private static final String POSITION = "POSITION";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ZOOM = "zoom";

    private static Activity activity;

    public static FragmentEcoMap newInstance(Activity activity_) {
        activity = activity_;
        return new FragmentEcoMap();
    }

    /**
     * Constructor
     */
    public FragmentEcoMap() {
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which is the default implementation).
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        manager = DataManager.getInstance(getActivity().getApplicationContext());
        fmanager = FilterManager.getInstance();
        manager.registerProblemListener(this);

        View rootView = inflater.inflate(R.layout.fragement_map, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();


        fmanager.registerFilterListener(this);

        MapsInitializer.initialize(getActivity().getApplicationContext());
        this.setUpMapIfNeeded();

        return rootView;
    }

    /**
     * Called when the fragment is no longer in use.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.removeProblemListener(this);
        fmanager.removeFilterListener(this);
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(FragmentEcoMap.POSITION, Context.MODE_PRIVATE).edit();
        editor.putFloat(LATITUDE, (float) googleMap.getCameraPosition().target.latitude);
        editor.putFloat(LONGITUDE, (float) googleMap.getCameraPosition().target.longitude);
        editor.putFloat(ZOOM, (float) googleMap.getCameraPosition().zoom);
        editor.apply();

    }

    /**
     * Get list of all problems.
     *
     * @param problems list of all problems.
     */
    @Override
    public void updateAllProblems(List<Problem> problems) {
        FragmentEcoMap.problems = problems;
        putAllProblemsOnMap(problems, null);
    }


    /**
     * Update filter
     *
     * @param filterState state of filter
     */
    public void updateFilterState(FilterState filterState) {
        putAllProblemsOnMap(problems, filterState);
    }

    /**
     * Get list of all details.
     *
     * @param details details of concrete problem.
     */
    @Override
    public void updateProblemDetails(Details details) {
        //TODO implement
    }

    /**
     * Puts all problems on map
     *
     * @param problems list of problems
     */
    public void putAllProblemsOnMap(final List<Problem> problems, final FilterState filterState) {
        clusterManager = new ClusterManager<>(getActivity().getApplicationContext(), googleMap);
        googleMap.setOnCameraChangeListener(clusterManager);
    //    googleMap.setOnMarkerClickListener(clusterManager);
        googleMap.setOnMarkerClickListener(new MarkerListener(activity));

        if (filterState != null) {
            googleMap.clear();
            for (Problem problem : problems) {
                if (filtration(filterState, problem)) {
                    clusterManager.addItem(problem);
                }
            }
        } else {
            clusterManager.addItems(problems);
        }
        clusterManager.setRenderer(new IconRenderer(getActivity(), googleMap, clusterManager));

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.
     */
    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            googleMap = mapView.getMap();
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    private void setUpMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings settings = googleMap.getUiSettings();
        settings.setMapToolbarEnabled(false);
        googleMap.setMyLocationEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        manager.getAllProblems();

        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(new LatLng(getActivity().getSharedPreferences(POSITION,
                        Context.MODE_PRIVATE).getFloat(LATITUDE, (float) INITIAL_POSITION.latitude),
                        getActivity().getSharedPreferences(POSITION,
                                Context.MODE_PRIVATE).getFloat(LONGITUDE, (float) INITIAL_POSITION.longitude)))
                .zoom(getActivity().getSharedPreferences(POSITION, Context.MODE_PRIVATE).getFloat(ZOOM, INITIAL_ZOOM))
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);
    }

    private boolean filtration(FilterState filterState, Problem problem) {
        if (filterState.isShowProblemType(problem.getProblemTypesId())) {
            if (((filterState.isShowResolvedProblem()) && (problem.getStatusId() == 1))
                    || (((filterState.isShowUnsolvedProblem()) && (problem.getStatusId() == 0)))) {
                int day = Integer.parseInt(problem.getDate().substring(8, 10));
                int year = Integer.parseInt(problem.getDate().substring(0, 4));
                int mounth = Integer.parseInt(problem.getDate().substring(5, 7)) - 1;
                Calendar creatingProblemDate = new GregorianCalendar(year, mounth, day);
                if ((creatingProblemDate.after(filterState.getDateFrom())) &&
                        (filterState.getDateTo().after(creatingProblemDate))) {
                    return true;
                }

            }
        }
         return false;
    }
}
