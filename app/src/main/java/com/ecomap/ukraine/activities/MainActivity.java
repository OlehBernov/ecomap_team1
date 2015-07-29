package com.ecomap.ukraine.activities;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
     * Drawer toggle.
     */
    private ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout2;

    Toolbar toolbar;
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

        drawerLayout2 = (DrawerLayout) findViewById(R.id.drawer2);
        drawerLayout2.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
     * Sets application toolbar.
     */
    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
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
     * Adds google map
     */
    private void addMapFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, FragmentEcoMap.newInstance())
                .commit();
    }

    boolean i = false;

    public void showFilter(MenuItem item) {
        drawerLayout2 = (DrawerLayout) findViewById(R.id.drawer2);
        if (!i) {
            drawerLayout2.openDrawer(GravityCompat.END);
            toolbar.setTitle("Filter");
            i = true;
        } else {
            drawerLayout2.closeDrawer(GravityCompat.END);
            toolbar.setTitle("Ecomap Ukraine");
            i = false;
        }
    }
}
