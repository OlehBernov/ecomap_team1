package com.ecomap.ukraine.activities.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.ecomap.ukraine.R;
import com.ecomap.ukraine.account.manager.AccountManager;
import com.ecomap.ukraine.activities.Authorization.LoginScreen;
import com.ecomap.ukraine.activities.Authorization.SignupActivity;
import com.ecomap.ukraine.settings.Settings;
import com.ecomap.ukraine.activities.addProblem.ChooseProblemLocationActivity;
import com.ecomap.ukraine.filter.FilterContract;
import com.ecomap.ukraine.filter.FilterManager;
import com.ecomap.ukraine.filter.FilterState;
import com.ecomap.ukraine.filter.FilterStateConverter;
import com.ecomap.ukraine.models.User;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Andriy on 01.07.2015.
 * <p/>
 * Main activity, represent GUI and provides access to all functional
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Name of the filter window.
     */
    private static final String FILTER = "Filter";

    private static final String DATE_FROM_TAG = "Date from tag";

    private static final String DATE_TO_TAG = "Date to tag";

    private static final String FILTERS_STATE = "Filters state";

    private static final String DATE_TEMPLATE = "dd-MM-yyyy";

    private static final String DEFAULT_DATE_FROM = "01-01-1990";

    private static final String DEFAULT_DATE_TO = "31-12-2030";

    private static final float ANCHOR_POINT = 0.3f;

    private static final String ALERT_MESSAGE =
            "To append a problem you must first be authorized. Authorize?";

    private static final String MAP_TAG = "Map tag";

    private static final String OK = "OK";

    private static final String CANCEL = "Cancel";

    private static final int LOG_IN_REQUEST_CODE = 1;

    private static final int SIGN_UP_REQUEST_CODE = 2;

    private static final int SETTINGS_REQUEST_CODE = 3;

    public ActionBarDrawerToggle drawerToggle;

    public boolean problemAddingMenu;

    private DrawerLayout filterLayout;

    private Toolbar toolbar;

    private CalendarDatePickerDialog dialogDateFrom;

    private CalendarDatePickerDialog dialogDateTo;

    private Calendar calendarDateFrom;

    private Calendar calendarDateTo;

    private SlidingUpPanelLayout slidingUpPanelLayout;

    private Menu menu;

    private Activity activity = this;

    private DrawerLayout menuDrawer;


    /**
     * Filter manager instance
     */
    private FilterManager filterManager;

    private CharSequence previousTitle;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            if (!filterLayout.isDrawerOpen(GravityCompat.END)) {

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (filterLayout.isDrawerOpen(GravityCompat.END)) {
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
        } else {
            super.onBackPressed();
        }
    }

    public static boolean isAnonymousUser() {
        return User.getInstance().getId() < 0;
    }

    /**
     * Controls the position of the filter7 window on the screen.
     */
    public void showFilter(MenuItem item) {
        if (!filterLayout.isDrawerOpen(GravityCompat.END)) {
            item.setIcon(R.drawable.ic_arrow_forward_white_36dp);
            setDate();
            filterLayout.openDrawer(GravityCompat.END);
            previousTitle = toolbar.getTitle();
            toolbar.setTitle(FILTER);
        } else {
            item.setIcon(R.drawable.ic_filter_list_white_36dp);
            toolbar.setTitle(R.string.app_name);
            filterLayout.closeDrawer(GravityCompat.END);

        }
    }

    public void switchCheckButtonState(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        filterManager.getFilterState(buildFiltersState());
    }

    public void dateFromChoosing(View view) {
        dialogDateFrom.show(getSupportFragmentManager(), DATE_FROM_TAG);
    }

    public void dateToChoosing(View view) {
        dialogDateTo.show(getSupportFragmentManager(), DATE_TO_TAG);
    }

    public void openChooseProblemLocationActivity(View view) {
        if (isAnonymousUser()) {
            setNotAuthorizeDialog();
        } else {
            Intent intent = new Intent(this, ChooseProblemLocationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void setNotAuthorizeDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.Caution)
                .content(ALERT_MESSAGE)
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
                        Intent mainIntent = new Intent(activity, LoginScreen.class);
                        startActivity(mainIntent);
                        finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void logIn(MenuItem item) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivityForResult(intent, LOG_IN_REQUEST_CODE);
        menuDrawer.closeDrawers();
    }

    public void logOut(MenuItem item) {
        User.reset();
        setUserInformation(User.getInstance());
        AccountManager.getInstance(getApplicationContext()).
                putUserToPreferences(User.getInstance(), "");
    }

    public void signUp(MenuItem item) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivityForResult(intent, SIGN_UP_REQUEST_CODE);
        menuDrawer.closeDrawers();
    }

    public void openSettings(MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        menuDrawer.closeDrawers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isSettingsRequest(requestCode)) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(MAP_TAG);
            if(fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            addMapFragment();
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
        menuDrawer = (DrawerLayout) findViewById(R.id.drawer);

        filterLayout = (DrawerLayout) findViewById(R.id.drawer2);
        filterManager = FilterManager.getInstance(this);
        setupToolbar();
        setUpDrawerLayout();
        setupFilter();

        setUserInformation(User.getInstance());

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setAnchorPoint(ANCHOR_POINT);
        slidingUpPanelLayout.setDragView(R.id.sliding_linear_layout);

        addMapFragment();

        createDateFromPickerDialog();
        createDateToPickerDialog();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        filterLayout.setDrawerListener(new FilterDrawerListener());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences settings = getApplicationContext().getSharedPreferences(FILTERS_STATE, 0);
        SharedPreferences.Editor editor = settings.edit();
        FilterState filterState = buildFiltersState();
        try {
            editor.putString(FILTERS_STATE, new FilterStateConverter().convertToJson(filterState));
        } catch (JSONException e) {
            Log.e("JSONException", "onDestroy");
        }
        editor.apply();
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

    private void setDateOnScreen(Calendar dateFrom, Calendar dateTo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);

        TextView dateFromView = (TextView) findViewById(R.id.date_from);
        String formattedDate = dateFormat.format(dateFrom.getTime());
        dateFromView.setText(formattedDate);

        TextView dateToView = (TextView) findViewById(R.id.date_to);
        formattedDate = dateFormat.format(dateTo.getTime());
        dateToView.setText(formattedDate);
    }

    private void setFilterOn(View view) {
        CheckBox checkBox = (CheckBox) view;
        checkBox.setChecked(false);
    }

    private void setUserInformation(User user) {
        TextView userName = (TextView) findViewById(R.id.navigation_user_name);
        TextView email = (TextView) findViewById(R.id.navigation_email);
        userName.setText(user.getName() + " " + user.getSurname());
        if(!isAnonymousUser()) {
            email.setText(user.getEmail());
        }
        else {
            email.setText("");
        }
    }

    private void createDateFromPickerDialog() {
        CalendarDatePickerDialog.OnDateSetListener dateFromListener =
                new CalendarDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialog dialog, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar date = Calendar.getInstance();
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, monthOfYear);
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (isValidDates(date, calendarDateTo)) {
                            calendarDateFrom = date;
                            setDate();
                            filterManager.getFilterState(buildFiltersState());
                        }
                    }
                };

        dialogDateFrom = new CalendarDatePickerDialog();
        dialogDateFrom.setOnDateSetListener(dateFromListener);
    }

    private boolean isValidDates(Calendar dateFrom, Calendar dateTo) {
        return dateTo.after(dateFrom);
    }

    private void createDateToPickerDialog() {
        CalendarDatePickerDialog.OnDateSetListener dateToListener =
                new CalendarDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialog dialog, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar date = Calendar.getInstance();
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, monthOfYear);
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (isValidDates(calendarDateFrom, date)) {
                            calendarDateTo = date;
                            setDate();
                            filterManager.getFilterState(buildFiltersState());
                        }
                    }
                };

        dialogDateTo = new CalendarDatePickerDialog();
        dialogDateTo.setOnDateSetListener(dateToListener);
    }

    private FilterState buildFiltersState() {
        Map<String, Boolean> filterStateValues = new HashMap<>();
        filterStateValues.put(FilterContract.FOREST_DESTRUCTION,
                getFilterState(R.id.forestry_issues));
        filterStateValues.put(FilterContract.RUBBISH_DUMP,
                getFilterState(R.id.rubbish_dump));
        filterStateValues.put(FilterContract.ILLEGAL_BUILDING,
                getFilterState(R.id.illegal_building));
        filterStateValues.put(FilterContract.WATER_POLLUTION,
                getFilterState(R.id.water_pollution));
        filterStateValues.put(FilterContract.THREAD_TO_BIODIVERSITY,
                getFilterState(R.id.biodiversity));
        filterStateValues.put(FilterContract.POACHING,
                getFilterState(R.id.poaching));
        filterStateValues.put(FilterContract.OTHER,
                getFilterState(R.id.other));
        filterStateValues.put(FilterContract.RESOLVED,
                getFilterState(R.id.ButtonResolved));
        filterStateValues.put(FilterContract.UNSOLVED,
                getFilterState(R.id.ButtonUnsolved));

        return new FilterState(filterStateValues, calendarDateFrom, calendarDateTo);
    }

    /**
     * Adds google map
     */
    private void addMapFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, FragmentEcoMap.newInstance(), MAP_TAG)
                .commit();
    }

    /**
     * Sets application toolbar.
     */
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setUpDrawerLayout() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);
    }

    private boolean getFilterState(int id) {
        CheckBox checkBox = (CheckBox) findViewById(id);
        return checkBox.isChecked();
    }

    /**
     * Adds google map
     */
    private void setupFilter() {
        filterLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        FilterState filterState = filterManager.getFilterStateFromPreference();
        setFiltersState(filterState);
    }

    private void setDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH);
        try {
            if (calendarDateFrom == null) {
                calendarDateFrom = Calendar.getInstance();
                calendarDateFrom.setTime(dateFormat.parse(DEFAULT_DATE_FROM));
            }
            if (calendarDateTo == null) {
                calendarDateTo = Calendar.getInstance();
                calendarDateTo.setTime(dateFormat.parse(DEFAULT_DATE_TO));
            }
        } catch (ParseException e) {
            Log.e("parseException", "setDate to");
            return;
        }

        setDateOnScreen(calendarDateFrom, calendarDateTo);
    }

    private void setFiltersState(FilterState filtersState) {
        if (filtersState != null) {
            if (filtersState.isFilterOn(FilterContract.FOREST_DESTRUCTION)) {
                setFilterOn(findViewById(R.id.forestry_issues));
            }
            if (filtersState.isFilterOn(FilterContract.RUBBISH_DUMP)) {
                setFilterOn(findViewById(R.id.rubbish_dump));
            }
            if (filtersState.isFilterOn(FilterContract.ILLEGAL_BUILDING)) {
                setFilterOn(findViewById(R.id.illegal_building));
            }
            if (filtersState.isFilterOn(FilterContract.WATER_POLLUTION)) {
                setFilterOn(findViewById(R.id.water_pollution));
            }
            if (filtersState.isFilterOn(FilterContract.THREAD_TO_BIODIVERSITY)) {
                setFilterOn(findViewById(R.id.biodiversity));
            }
            if (filtersState.isFilterOn(FilterContract.POACHING)) {
                setFilterOn(findViewById(R.id.poaching));
            }
            if (filtersState.isFilterOn(FilterContract.OTHER)) {
                setFilterOn(findViewById(R.id.other));
            }
            if (filtersState.isFilterOn(FilterContract.RESOLVED)) {
                setFilterOn(findViewById(R.id.ButtonResolved));
            }
            if (filtersState.isFilterOn(FilterContract.UNSOLVED)) {
                setFilterOn(findViewById(R.id.ButtonUnsolved));
            }
            calendarDateFrom = filtersState.getDateFrom();
            calendarDateTo = filtersState.getDateTo();
        }
        setDate();
    }

    private class FilterDrawerListener extends DrawerLayout.SimpleDrawerListener {
        @Override
        public void onDrawerClosed(View view) {
            filterManager.setRenderer();
        }
    }
}
