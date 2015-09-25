package com.ecomap.ukraine.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.authentication.manager.AccountManager;
import com.ecomap.ukraine.models.User;
import com.ecomap.ukraine.ui.fragments.FilterFragment;
import com.ecomap.ukraine.ui.fragments.FragmentEcoMap;
import com.ecomap.ukraine.update.manager.DataManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


/**
 * Created by Andriy on 01.07.2015.
 * <p/>
 * Main activity, represent GUI and provides access to all functional
 */
public class MainActivity extends AppCompatActivity {

    private static final String FILTER = "Filter";
    private static final float ANCHOR_POINT = 0.3f;
    private static final String ALERT_MESSAGE =
            "To append a problem you must first be authorized. Authorize?";
    private static final String MAP_TAG = "Map tag";
    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";

    private static final int LOG_IN_REQUEST_CODE = 1;
    private static final int SIGN_UP_REQUEST_CODE = 2;
    private static final int SETTINGS_REQUEST_CODE = 3;
    public boolean problemAddingMenu;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout filterLayout;
    private Toolbar toolbar;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private Menu menu;
    private Activity activity = this;
    private DrawerLayout menuDrawer;
    private AccountManager accountManager;
    private CharSequence previousTitle;
    private DrawerLayout drawerLayout;
    private FilterFragment filterFragment;
    private FragmentEcoMap mapFragment;

    /**
     * Inflate the menu, this adds items to the action bar if it is present.
     *
     * @param menu activity menu
     * @return result of action
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    /**
     * Called when the user selects an item from the options menu
     *
     * @param item menu item
     * @return result of action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    /**
     * Controls the position of the filter7 window on the screen.
     */
    public void showFilter(MenuItem item) {
        if (!filterLayout.isDrawerOpen(GravityCompat.END)) {
            item.setIcon(R.drawable.ic_arrow_forward_white_36dp);
            filterFragment.setDate();
            filterLayout.openDrawer(GravityCompat.END);
            previousTitle = toolbar.getTitle();
            toolbar.setTitle(FILTER);
        } else {
            item.setIcon(R.drawable.ic_filter_list_white_36dp);
            toolbar.setTitle(R.string.app_name);
            filterLayout.closeDrawer(GravityCompat.END);
        }
    }

    /**
     * Changed checkBox state on filter layout.
     *
     * @param view view of the checkBox
     */
    public void switchCheckButtonState(View view) {
        filterFragment.switchCheckButtonState(view);
    }

    public void dateFromChoosing(View view) {
        filterFragment.dateFromChoosing(getSupportFragmentManager());
    }

    /**
     * Changed date state on filter layout
     *
     * @param view date view
     */
    public void dateToChoosing(View view) {
        filterFragment.dateToChoosing(getSupportFragmentManager());
    }

    /**
     * Opens ChooseProblemLocationActivity on Click
     *
     * @param view view that was clicked
     */
    public void openChooseProblemLocationActivity(View view) {
        if (accountManager.isAnonymousUser()) {
            setNotAuthorizeDialog(ALERT_MESSAGE);
        } else {
            Intent intent = new Intent(this, ChooseProblemLocationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Set dialog when user are not authorized
     *
     * @param message how to solve the problem
     */
    public void setNotAuthorizeDialog(final String message) {
        new MaterialDialog.Builder(this)
                .title(R.string.Caution)
                .content(message)
                .backgroundColorRes(R.color.log_in_dialog)
                .contentColorRes(R.color.log_in_content)
                .negativeColorRes(R.color.log_in_content)
                .titleColorRes(R.color.log_in_title)
                .cancelable(false)
                .positiveText(OK)
                .negativeText(CANCEL).callback(
                new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        Intent mainIntent = new Intent(activity, LoginActivity.class);
                        startActivity(mainIntent);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.cancel();
                    }
                })
                .show();
    }

    /**
     * Opens Login activity
     *
     * @param item menu item which was clicked
     */
    public void logIn(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOG_IN_REQUEST_CODE);
        menuDrawer.closeDrawers();
    }

    /**
     * Log out user
     *
     * @param item menu item which was clicked
     */
    public void logOut(MenuItem item) {
        setUserInformation(User.ANONYM_USER);
        accountManager.putUser(User.ANONYM_USER);
    }

    /**
     * Opens SignUp activity
     *
     * @param item menu item which was clicked
     */
    public void signUp(MenuItem item) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivityForResult(intent, SIGN_UP_REQUEST_CODE);
        menuDrawer.closeDrawers();
    }

    /**
     * Opens Setting activity
     *
     * @param item menu item which was clicked
     */
    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        menuDrawer.closeDrawers();
    }

    /**
     * Opens SearchActivity activity
     *
     * @param item menu item which was clicked
     */
    public void openSearch(MenuItem item) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        menuDrawer.closeDrawers();
    }

    public void openTop10Activity(MenuItem item) {
        Intent intent = new Intent(this, Top10Activity.class);
        startActivity(intent);
        menuDrawer.closeDrawers();
    }

    /**
     * Called when an activity you launched exits.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult().
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setUserInformation(accountManager.getUser());
        if (isSettingsRequest(requestCode)) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(MAP_TAG);
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            addMapFragment();
            addFilterFragment();
        }
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (filterLayout.isDrawerOpen(GravityCompat.END)) {
            menu.findItem(R.id.action_find_location)
                    .setIcon(R.drawable.ic_filter_list_white_36dp);
            filterLayout.closeDrawer(GravityCompat.END);
            toolbar.setTitle(previousTitle);
        } else if (isInformationalPanelExpanded()) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout
                    .PanelState.ANCHORED);
        } else if (isInformationalPanelCollapsed()) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout
                    .PanelState.HIDDEN);
            DataManager.getInstance(getApplicationContext()).removeAllListeners();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Initialize activity
     *
     * @param savedInstanceState Contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();

        menuDrawer = (DrawerLayout) findViewById(R.id.drawer);
        setUpDrawerLayout();

        filterLayout = (DrawerLayout) findViewById(R.id.drawer2);
        filterLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        filterLayout.setDrawerListener(new FilterDrawerListener());

        accountManager = AccountManager.getInstance(this);
        setUserInformation(accountManager.getUser());

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setAnchorPoint(ANCHOR_POINT);
        slidingUpPanelLayout.setDragView(R.id.sliding_linear_layout);

        addMapFragment();
        addFilterFragment();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * Sync the toggle state after onRestoreInstanceState has occurred.
     *
     * @param savedInstanceState saved application state.
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
     * The final call you receive before your activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterFragment.saveState(this);
    }

    /**
     * Sets filter fragment on filter view.
     */
    private void addFilterFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        filterFragment = FilterFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.filter_container, filterFragment, MAP_TAG)
                .commit();
    }

    private boolean isSettingsRequest(int requestCode) {
        return requestCode == SETTINGS_REQUEST_CODE;
    }

    private boolean isInformationalPanelCollapsed() {
        return slidingUpPanelLayout.getPanelState()
                != SlidingUpPanelLayout.PanelState.COLLAPSED;
    }

    private boolean isInformationalPanelExpanded() {
        return slidingUpPanelLayout.getPanelState()
                == SlidingUpPanelLayout.PanelState.EXPANDED;
    }

    /**
     * Sets user information on drawer
     *
     * @param user user state
     */
    private void setUserInformation(User user) {
        TextView userName = (TextView) findViewById(R.id.navigation_user_name);
        TextView email = (TextView) findViewById(R.id.navigation_email);
        userName.setText(user.getName() + " " + user.getSurname());
        if (!accountManager.isAnonymousUser()) {
            email.setText(user.getEmail());
        } else {
            email.setText("");
        }
    }

    /**
     * Adds google map
     */
    private void addMapFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = new FragmentEcoMap();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mapFragment, MAP_TAG)
                .commit();
    }

    /**
     * Sets application toolbar.
     */
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Setup drawer layout
     */
    private void setUpDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);
    }

    /**
     * Handles when filter drawer are closed
     */
    private class FilterDrawerListener extends DrawerLayout.SimpleDrawerListener {
        @Override
        public void onDrawerClosed(View view) {
            mapFragment.setRenderer();
        }
    }

}
