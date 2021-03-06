package com.ecomap.ukraine.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.ui.fragments.FragmentChooseCoordMap;
import com.ecomap.ukraine.util.ExtraFieldNames;

public class ChooseProblemLocationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FragmentChooseCoordMap map;

    /**
     * Open AddDescriptionActivity
     */
    public void openAddDescriptionActivity(View view) {
        if (map.getMarkerPosition() == null) {
            Toast.makeText(this, R.string.Tap_message, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent mainIntent = new Intent(this, AddNewProblemDecriptionActivity.class);
        mainIntent.putExtra(ExtraFieldNames.LAT, map.getMarkerPosition().latitude);
        mainIntent.putExtra(ExtraFieldNames.LNG, map.getMarkerPosition().longitude);
        startActivity(mainIntent);
        finish();
    }

    /**
     * Calls when pressed custom back button
     */
    public void cancelButton(View view) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    /**
     * Calls when pressed back button
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelButton(null);
    }

    /**
     * Initialize activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_coordinate_layout);
        map = new FragmentChooseCoordMap();

        addMapFragment();
        setupToolbar();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButton(v);

            }
        });
    }

    /**
     * Adds google map
     */
    private void addMapFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.map_container, map)
                .commit();
    }

    /**
     * Set custom setting on toolbar
     */
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.Choose_problem_location_activity);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setClickable(true);
    }

}
