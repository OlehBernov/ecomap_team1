package com.ecomap.ukraine.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Andriy on 11.08.2015.
 */
public class ChooseProblemLocationActivity extends AppCompatActivity {

    private Intent mainIntent;
    Toolbar toolbar;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_coordinate_layout);
        user = (User) getIntent().getSerializableExtra("User");

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
                .replace(R.id.map_container, new FragmentChooseCoordMap())
                .commit();
    }


    public void openAddDescriptionActivity (View view) {
        mainIntent = new Intent(this, AddNewProblemDecriptionActivity.class);
        mainIntent.putExtra("User", user);
        startActivity(mainIntent);
        finish();

    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Choose problem location");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setClickable(true);

    }

    public void cancelButton (View view) {

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra("User", user);
        startActivity(mainIntent);
        finish();
    }
@Override
    public void onBackPressed() {
    super.onBackPressed();
    cancelButton(null);
    }

}
