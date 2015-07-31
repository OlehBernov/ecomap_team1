package com.ecomap.ukraine.activities;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ecomap.ukraine.R;

/**
 * Created by Andriy on 01.07.2015.
 *
 * Main activity, represent GUI and provides access to all functional
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Name of the filter window.
     */
    private static final String FILTER = "Filter";

    /**
     * Name of map window.
     */
    private static final String ECOMAP = "Ecomap Ukraine";

    /**
     * Drawer toggle.
     */
    private ActionBarDrawerToggle drawerToggle;

    /**
     * Filter layout.
     */
    private DrawerLayout filterLayout;

    /**
     * Application toolbar.
     */
    private Toolbar toolbar;

    /**
     * Initialize activity
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        setupToolbar();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                                                 R.string.drawer_open,
                                                 R.string.drawer_close);

        filterLayout = (DrawerLayout) findViewById(R.id.drawer2);
        filterLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.addMapFragment();
    }

    /**
     *  Sync the toggle state after onRestoreInstanceState has occurred.
     *
     *  @param savedInstanceState saved application state.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * Sync the toggle state after change application configuration.
     *
     * @param newConfig new application configuration.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Handle action bar items.
     * @param item menu item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Inflate the menu, this adds items to the action bar if it is present.
     * @param menu activity menu
     * @return result of action
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Sets application toolbar.
     */
    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Controls the position of the filter7 window on the screen.
     *
     */
    public void showFilter(MenuItem item) {
        filterLayout = (DrawerLayout) findViewById(R.id.drawer2);
        if (!filterLayout.isDrawerOpen(GravityCompat.END)) {
            filterLayout.openDrawer(GravityCompat.END);
            toolbar.setTitle(FILTER);
        } else {
            filterLayout.closeDrawer(GravityCompat.END);
            toolbar.setTitle(ECOMAP);
        }
    }

    /**
     * Adds google map
     */
    private void addMapFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, FragmentEcoMap.newInstance())
                .commit();
    }

}
