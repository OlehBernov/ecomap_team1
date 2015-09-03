package com.ecomap.ukraine.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.helper.ExtraFieldNames;
import com.ecomap.ukraine.MainActivity;
import com.ecomap.ukraine.problemdetails.DetailsContent;
import com.ecomap.ukraine.problemdetails.DetailsSettings;
import com.ecomap.ukraine.updateproblem.manager.DataManager;
import com.ecomap.ukraine.updateproblem.manager.ProblemListener;
import com.ecomap.ukraine.filter.Filter;
import com.ecomap.ukraine.filter.FilterListener;
import com.ecomap.ukraine.filter.FilterManager;
import com.ecomap.ukraine.filter.FilterState;
import com.ecomap.ukraine.model.Details;
import com.ecomap.ukraine.model.Problem;
import com.ecomap.ukraine.settings.MapType;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

/**
 * Represent map functionality
 */
public class FragmentEcoMap extends android.support.v4.app.Fragment
        implements ProblemListener, FilterListener,
        ClusterManager.OnClusterItemClickListener<Problem>,
        ClusterManager.OnClusterClickListener <Problem> {

    private static final LatLng INITIAL_POSITION = new LatLng(48.4, 31.2);
    private static final float INITIAL_ZOOM = 4.5f;
    private static final float ON_MARKER_CLICK_ZOOM = 12;
    private static final float ON_MY_POSITION_CLICK_ZOOM = 15;
    private static final int MAP_TYPE_NORMAL_ID = MapType.MAP_TYPE_NORMAL.getId();

     /**
     * The name of the preference to retrieve.
     */
    private static final String POSITION = "POSITION";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ZOOM = "zoom";

    private static List<Problem> problems;
    private static ClusterManager<Problem> clusterManager;

    private MapView mapView;
    private GoogleMap googleMap;
    private DataManager dataManager;
    private int mapType;
    private FilterManager filterManager;
    private DetailsContent detailsContent;

    public FragmentEcoMap() {
    }

    public static FragmentEcoMap newInstance() {
        return new FragmentEcoMap();
    }

    /**
     * Puts all problems on map
     *
     * @param filterState State of filter
     */
    public void putAllProblemsOnMap(FilterState filterState) {
        googleMap.clear();
        if (filterState == null) {
            filterState = filterManager.getFilterStateFromPreference();
        }
        setupClusterManager(filterState);
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
        dataManager = DataManager.getInstance(getActivity().getApplicationContext());
      //  dataManager.registerProblemListener(this);

        detailsContent = (DetailsContent) getActivity().findViewById(R.id.panel_details_content);

        View rootView = inflater.inflate(R.layout.fragement_map, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        FloatingActionButton ukrainePositionButton = (FloatingActionButton) getActivity().
                findViewById(R.id.fabUkraine);
        ukrainePositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPosition cameraPosition = new CameraPosition
                        .Builder()
                        .target(INITIAL_POSITION)
                        .zoom(INITIAL_ZOOM)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);

                }
            });

        FloatingActionButton myPositionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        myPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location myLocation = getLocation();
                if (myLocation != null) {
                    moveCameraToMyLocation(myLocation);
                }
            }

            @Nullable
            private Location getLocation() {
                Location myLocation = null;
                LocationManager locationManager =
                        (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = locationManager.getAllProviders();
                for (String provider : providers) {
                    Location location = locationManager.getLastKnownLocation(provider);
                    if (location != null) {
                        myLocation = location;
                    }
                }
                return myLocation;
            }
        });

        filterManager = FilterManager.getInstance(getActivity());
        filterManager.registerFilterListener(this);

        MapsInitializer.initialize(getActivity().getApplicationContext());

        SharedPreferences mapTypePreference = getActivity().getApplicationContext()
                .getSharedPreferences(ExtraFieldNames.MAP_TYPE, Context.MODE_PRIVATE);
        mapType = mapTypePreference.getInt(ExtraFieldNames.MAP_TYPE_ID, 0);
        setUpMapIfNeeded();

        return rootView;
    }

    /**
     * Called when the fragment is no longer in use.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        dataManager.removeProblemListener(this);
        filterManager.removeFilterListener(this);
        SharedPreferences.Editor editor = getActivity()
                .getSharedPreferences(FragmentEcoMap.POSITION,
                        Context.MODE_PRIVATE).edit();
        editor.putFloat(LATITUDE, (float) googleMap.getCameraPosition().target.latitude);
        editor.putFloat(LONGITUDE, (float) googleMap.getCameraPosition().target.longitude);
        editor.putFloat(ZOOM, googleMap.getCameraPosition().zoom);
        editor.apply();
    }

    /**
     * Get list of all problems.
     *
     * @param problems list of all problems.
     */
    @Override
    public void updateAllProblems(final List<Problem> problems) {
        FragmentEcoMap.problems = problems;
        putAllProblemsOnMap(null);
        setRenderer();
        dataManager.removeProblemListener(this);
    }

    /**
     * Update filter
     *
     * @param filterState state of filter
     */
    @Override
    public void updateFilterState(final FilterState filterState) {
        putAllProblemsOnMap(filterState);
    }

    /**
     * Sets renderer on map
     */
    @Override
    public void setRenderer () {
        clusterManager.setRenderer(new IconRenderer(getActivity(), googleMap, clusterManager));
    }

    /**
     * Get list of all details.
     *
     * @param details details of concrete problem.
     */
    @Override
    public void updateProblemDetails(final Details details) {
        detailsContent.setProblemDetails(details);
        dataManager.removeProblemListener(this);
    }

    /**
     * Action after click on Marker
     *
     * @param problem clicked marker
     * @return result of action
     */
    @Override
    public boolean onClusterItemClick(final Problem problem) {
        if (!((MainActivity) getActivity()).problemAddingMenu) {
            dataManager.registerProblemListener(this);
            DetailsSettings detailsSettings = new DetailsSettings(getActivity(), problem);
            detailsContent.setProblemContent(problem, detailsSettings);
            dataManager.getProblemDetail(problem.getProblemId());
            moveCameraToProblem(problem);
            return true;
        }
        return false;
    }

    /**
     * Action with click on cluster
     *
     * @param cluster clicked cluster
     * @return result of action
     */
    @Override
    public boolean onClusterClick(Cluster<Problem> cluster) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(),
                googleMap.getCameraPosition().zoom + 2));
        return true;
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
        setMapType();
        UiSettings settings = googleMap.getUiSettings();
        settings.setMapToolbarEnabled(false);
        googleMap.setMyLocationEnabled(true);
        settings.setMyLocationButtonEnabled(true);

        dataManager.registerProblemListener(this);
        dataManager.getAllProblems();

        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(new LatLng(
                        getActivity()
                                .getSharedPreferences(POSITION, Context.MODE_PRIVATE)
                                .getFloat(LATITUDE, (float) INITIAL_POSITION.latitude),
                        getActivity()
                                .getSharedPreferences(POSITION, Context.MODE_PRIVATE)
                                .getFloat(LONGITUDE, (float) INITIAL_POSITION.longitude)))
                .zoom(getActivity().getSharedPreferences(POSITION, Context.MODE_PRIVATE).getFloat(ZOOM, INITIAL_ZOOM))
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);
    }

    /**
     * Change camera position to clicked marker
     *
     * @param problem clicked marker
     */
    private void moveCameraToProblem(final Problem problem) {
        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(new LatLng(problem.getPosition().latitude, problem.getPosition().longitude))
                .zoom(ON_MARKER_CLICK_ZOOM)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);
    }

    /**
     * Change camera position to user location
     *
     * @param myLocation user location
     */
    private void moveCameraToMyLocation(final Location myLocation) {
        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                .zoom(ON_MY_POSITION_CLICK_ZOOM)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);
    }

    /**
     * Setup base parameters on cluster manager
     *
     * @param filterState state of filter
     */
    private void setupClusterManager(final FilterState filterState) {
        List<Problem> filteredProblems = new Filter().filterProblem(problems, filterState);
        clusterManager =
                new ClusterManager<>(getActivity().getApplicationContext(), googleMap);
        googleMap.setOnCameraChangeListener(clusterManager);
        clusterManager.setOnClusterItemClickListener(this);
        clusterManager.setOnClusterClickListener(this);
        googleMap.setOnMarkerClickListener(clusterManager);
        clusterManager.addItems(filteredProblems);
    }

    /**
     * Sets map type
     */
    private void setMapType() {
        if(mapType == MAP_TYPE_NORMAL_ID) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
    }

}