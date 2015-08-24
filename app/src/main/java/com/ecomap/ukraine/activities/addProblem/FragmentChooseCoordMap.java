package com.ecomap.ukraine.activities.addProblem;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ecomap.ukraine.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class FragmentChooseCoordMap extends android.support.v4.app.Fragment {

    private static final float ON_MY_POSITION_CLICK_ZOOM = 15;
    private static final LatLng INITIAL_POSITION = new LatLng(48.4, 31.2);
    private static final float INITIAL_ZOOM = 4.5f;
    private GoogleMap googleMap;
    private MapView mapView;
    private LatLng markerPosition;

    public LatLng getMarkerPosition() {
        return markerPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragement_map, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        ImageButton myPositionButton = (ImageButton) getActivity().findViewById(R.id.position_button);
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

        MapsInitializer.initialize(getActivity().getApplicationContext());
        setUpMapIfNeeded();
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                markerPosition = marker.getPosition();
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (latLng != null) {
                    googleMap.clear();
                }
                googleMap.addMarker(new MarkerOptions()
                        .draggable(true).position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                        .anchor(0.5F, 1));
                markerPosition = latLng;

            }
        });

        moveCameraToInitialPosition();

        return rootView;
    }

    private void moveCameraToInitialPosition() {
        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(INITIAL_POSITION)
                .zoom(INITIAL_ZOOM)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);
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

    private void setUpMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings settings = googleMap.getUiSettings();
        settings.setMapToolbarEnabled(false);
        googleMap.setMyLocationEnabled(true);
        settings.setMyLocationButtonEnabled(false);

    }

    private void moveCameraToMyLocation(Location myLocation) {
        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                .zoom(ON_MY_POSITION_CLICK_ZOOM)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);
    }
}
