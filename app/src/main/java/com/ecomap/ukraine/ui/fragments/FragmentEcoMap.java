package com.ecomap.ukraine.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.filtration.Filter;
import com.ecomap.ukraine.filtration.FilterListener;
import com.ecomap.ukraine.filtration.FilterManager;
import com.ecomap.ukraine.filtration.FilterState;
import com.ecomap.ukraine.map.IconRenderer;
import com.ecomap.ukraine.map.MapType;
import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.ui.DetailsController;
import com.ecomap.ukraine.ui.activities.MainActivity;
import com.ecomap.ukraine.ui.fullinfo.DetailsContent;
import com.ecomap.ukraine.update.manager.DataListenerAdapter;
import com.ecomap.ukraine.update.manager.DataManager;
import com.ecomap.ukraine.util.BasicContentLayout;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

/**
 * Represent map functionality
 */
public class FragmentEcoMap extends android.support.v4.app.Fragment
        implements FilterListener,
        ClusterManager.OnClusterItemClickListener<Problem>,
        ClusterManager.OnClusterClickListener<Problem> {

    private static final LatLng INITIAL_POSITION = new LatLng(48.4, 31.2);
    private static final float INITIAL_ZOOM = 4.5f;
    private static final float ON_MARKER_CLICK_ZOOM = 12;
    private static final float ON_MY_POSITION_CLICK_ZOOM = 12;

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
    private FilterManager filterManager;
    private DetailsContent detailsContent;
    private int mapType;
    private DataListenerAdapter dataListenerAdapter;

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
            filterState = filterManager.getCurrentFilterState();
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

        dataManager = DataManager.getInstance(getActivity());
        dataListenerAdapter = new DataListenerAdapter() {
            /**
             * Get list of all problems.
             *
             * @param problems list of all problems.
             */
            @Override
            public void onAllProblemsUpdate(final List<Problem> problems) {
                FragmentEcoMap.problems = problems;
                putAllProblemsOnMap(null);
                setRenderer();
            }

            /**
             * Receive object of problem details.
             * @param details object of problem details.
             */
            @Override
            public void onProblemDetailsUpdate(final Details details) {
                if (detailsContent != null) {
                    detailsContent.prepareToRefresh();
                    detailsContent.setProblemDetails(details);
                }
            }
        };
        dataManager.registerProblemListener(dataListenerAdapter);

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
            }            @Override
            public void onClick(View v) {
                Location myLocation = getLocation();
                if (myLocation != null) {
                    moveCameraToMyLocation(myLocation);
                }
            }


        });

        filterManager = FilterManager.getInstance(getActivity());
        filterManager.registerFilterListener(this);

        MapsInitializer.initialize(getActivity());

        String mapTypeSharedPreferences = getResources().getString(R.string.map_type);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mapType = Integer.valueOf(sharedPreferences.getString(mapTypeSharedPreferences, "0"));
        setUpMapIfNeeded();

        return rootView;
    }

    /**
     * Called when the fragment is no longer in use.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        dataManager.removeProblemListener(dataListenerAdapter);
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
    public void setRenderer() {
        clusterManager.setRenderer(new IconRenderer(getActivity(), googleMap, clusterManager));
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
            LinearLayout detailsRoot = (LinearLayout) getActivity().findViewById(R.id.pain);
            BasicContentLayout basicContentLayout = new BasicContentLayout(detailsRoot);
            detailsContent = new DetailsContent(basicContentLayout, getActivity());
            detailsContent.setBaseInfo(problem);
            new DetailsController(getActivity(), problem);
            dataManager.getProblemDetail(problem.getProblemId());
            moveCameraToProblem(problem);
            return true;
        }
        return false;
    }


    @Override
    public boolean onClusterClick(Cluster<Problem> cluster) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getClusterCenter(cluster),
                calculateNewZoom(cluster)));
        return true;
    }

    private LatLng getClusterCenter(Cluster<Problem> cluster) {
        return getClusterLatLngBounds(cluster).getCenter();
    }

    private float calculateNewZoom(Cluster<Problem> cluster) {
        LatLngBounds screenBounds = getScreenLatLngBounds();
        LatLngBounds clusterBounds = getClusterLatLngBounds(cluster);
        return getCameraZoom(screenBounds, clusterBounds);
    }

    private float getCameraZoom(LatLngBounds screenBounds, LatLngBounds clusterBounds) {
        double latitudeClusterDifference = getLatSize(clusterBounds);
        double longitudeClusterDifference = getLngSize(clusterBounds);
        double latitudeScreenDifference = getLatSize(screenBounds);
        double longitudeScreenDifference = getLngSize(screenBounds);

        if (Math.abs(longitudeClusterDifference / longitudeScreenDifference)
                > Math.abs(latitudeClusterDifference / latitudeScreenDifference)) {
            return (float) (googleMap.getCameraPosition().zoom
                    + zoomIncreaseValue(longitudeClusterDifference, longitudeScreenDifference));
        } else {
            return (float) (googleMap.getCameraPosition().zoom
                    + zoomIncreaseValue(latitudeClusterDifference, latitudeScreenDifference));
        }
    }

    private double getLngSize(LatLngBounds clusterBounds) {
        double northEastLng = clusterBounds.northeast.longitude;
        double southWestLng = clusterBounds.southwest.longitude;
        return northEastLng - southWestLng;
    }

    private double getLatSize(LatLngBounds clusterBounds) {
        double northEastLat = clusterBounds.northeast.latitude;
        double southWestLat = clusterBounds.southwest.latitude;
        return northEastLat - southWestLat;
    }

    private double zoomIncreaseValue(double clusterMaxDifference, double screenMaxDifference) {
        return log(Math.abs(screenMaxDifference / clusterMaxDifference) * 0.7, 2);
    }

    private LatLngBounds getScreenLatLngBounds() {
        Projection projection = googleMap.getProjection();
        return projection.getVisibleRegion().latLngBounds;
    }

    private LatLngBounds getClusterLatLngBounds(Cluster<Problem> cluster) {
        LatLngBounds.Builder clusterBoundsBuilder = LatLngBounds.builder();
        for (Problem problem : cluster.getItems()) {
            clusterBoundsBuilder.include(problem.getPosition());
        }
        return clusterBoundsBuilder.build();
    }

    private double log(double num, double base) {
        return Math.log10(num) / Math.log10(base);
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
        List<Problem> filteredProblems = Filter.filterProblem(problems, filterState);
        clusterManager = new ClusterManager<>(getActivity(), googleMap);
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
        if (mapType == MapType.MAP_TYPE_NORMAL) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
    }

}
